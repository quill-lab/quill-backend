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
import java.util.*

data class TestAccount(
    val email: String,
    val password: String,
    val nickname: String,
    val accessToken: String,
)

abstract class AuthControllerTest(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) : ControllerTest(mockMvc, objectMapper) {

    protected fun performAuthGet(url: String, account: TestAccount? = null) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.get(url), account)
            .accept(MediaType.APPLICATION_JSON)
    ).andDo(MockMvcResultHandlers.print())

    protected fun performAuthPost(url: String, body: Any, account: TestAccount? = null) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.post(url), account)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(body))
    ).andDo(MockMvcResultHandlers.print())

    protected fun performAuthPut(url: String, body: Any, account: TestAccount? = null) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.put(url), account)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(body))
    ).andDo(MockMvcResultHandlers.print())

    protected fun performAuthDelete(url: String, account: TestAccount? = null) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.delete(url), account)
            .accept(MediaType.APPLICATION_JSON)
    ).andDo(MockMvcResultHandlers.print())

    private fun addAuthHeader(
        builder: MockHttpServletRequestBuilder,
        account: TestAccount?
    ): MockHttpServletRequestBuilder {
        val testAccount = account ?: fixtureAccount()
        return builder.header(HttpHeaders.AUTHORIZATION, "Bearer ${testAccount.accessToken}")
    }

    protected fun fixtureAccount(): TestAccount {
        val uuid = UUID.randomUUID().toString()
        val email = "test$uuid@test.com"
        val password = "Password1!"
        val nickname = "test123"

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    toJson(
                        mapOf(
                            "email" to email,
                            "password" to password,
                            "nickname" to nickname
                        )
                    )
                )
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val token = signIn(email, password)
        return TestAccount(
            email = email,
            password = password,
            nickname = nickname,
            accessToken = token
        )
    }

    private fun signIn(email: String, password: String): String {
        val signInResult = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/sign-in")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    toJson(
                        mapOf(
                            "email" to email,
                            "password" to password
                        )
                    )
                )
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andReturn()

        val response = objectMapper.readValue(signInResult.response.contentAsString, SignInResponse::class.java)
        return response.accessToken
    }

    protected fun fixtureNovelRoom(account: TestAccount?): Long {
        val request = mapOf(
            "maxContributors" to 5,
            "title" to "test title",
            "description" to "test description",
            "category" to "GENERAL",
            "tags" to listOf("test tag"),
            "synopsis" to "test synopsis",
            "coverImage" to "test cover image"
        )

        val response = performAuthPost("/api/v1/novel-rooms", request, account)
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andReturn()

        val responseBody = objectMapper.readTree(response.response.contentAsString)
        return responseBody.get("id").asLong()
    }
}
