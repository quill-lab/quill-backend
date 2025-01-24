package lab.ujumeonji.literaturebackend.support

import com.fasterxml.jackson.databind.ObjectMapper
import lab.ujumeonji.literaturebackend.api.auth.dto.SignInResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.UUID

abstract class AuthControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) : ControllerTest(mockMvc, objectMapper) {

    protected fun performAuthGet(url: String) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.get(url))
            .accept(MediaType.APPLICATION_JSON)
    ).andDo(MockMvcResultHandlers.print())

    protected fun performAuthPost(url: String, body: Any) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.post(url))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(body))
    ).andDo(MockMvcResultHandlers.print())

    protected fun performAuthPut(url: String, body: Any) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.put(url))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(body))
    ).andDo(MockMvcResultHandlers.print())

    protected fun performAuthDelete(url: String) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.delete(url))
            .accept(MediaType.APPLICATION_JSON)
    ).andDo(MockMvcResultHandlers.print())

    private fun addAuthHeader(builder: MockHttpServletRequestBuilder): MockHttpServletRequestBuilder {
        val (email, password) = createTestAccount()
        val token = signIn(email, password)
        return builder.header(HttpHeaders.AUTHORIZATION, "Bearer $token")
    }

    private fun createTestAccount(): Pair<String, String> {
        val uuid = UUID.randomUUID().toString()
        val email = "test$uuid@test.com"
        val password = "Password1!"
        val nickname = "tester$uuid"

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(mapOf(
                    "email" to email,
                    "password" to password,
                    "nickname" to nickname
                )))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        return email to password
    }

    private fun signIn(email: String, password: String): String {
        val signInResult = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(mapOf(
                    "email" to email,
                    "password" to password
                )))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val response = objectMapper.readValue(signInResult.response.contentAsString, SignInResponse::class.java)
        return response.accessToken
    }
}
