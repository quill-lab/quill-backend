package lab.ujumeonji.literaturebackend.support

import com.fasterxml.jackson.databind.ObjectMapper
import lab.ujumeonji.literaturebackend.support.extend.fixtureAccount
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

data class TestAccount(
    val email: String,
    val password: String,
    val nickname: String,
    val accessToken: String,
)

abstract class AuthControllerTest(
    protected val mockMvc: MockMvc,
    objectMapper: ObjectMapper,
) : ControllerTest(mockMvc, objectMapper) {

    fun performAuthGet(url: String, account: TestAccount? = null) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.get(url), account)
            .accept(MediaType.APPLICATION_JSON)
    ).andDo(MockMvcResultHandlers.print())

    fun performAuthPost(url: String, body: Any, account: TestAccount? = null) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.post(url), account)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(body))
    ).andDo(MockMvcResultHandlers.print())

    fun performAuthPut(url: String, body: Any, account: TestAccount? = null) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.put(url), account)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(body))
    ).andDo(MockMvcResultHandlers.print())

    fun performAuthDelete(url: String, account: TestAccount? = null) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.delete(url), account)
            .accept(MediaType.APPLICATION_JSON)
    ).andDo(MockMvcResultHandlers.print())

    private fun addAuthHeader(
        builder: MockHttpServletRequestBuilder,
        account: TestAccount?
    ): MockHttpServletRequestBuilder {
        val testAccount = account ?: this.fixtureAccount()
        return builder.header(HttpHeaders.AUTHORIZATION, "Bearer ${testAccount.accessToken}")
    }
}
