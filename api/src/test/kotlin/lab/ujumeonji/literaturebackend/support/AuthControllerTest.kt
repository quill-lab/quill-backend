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
    val id: String,
    val email: String,
    val password: String,
    val nickname: String,
    val token: String,
)

abstract class AuthControllerTest(
    protected val mockMvc: MockMvc,
    objectMapper: ObjectMapper,
) : ControllerTest(mockMvc, objectMapper) {
    fun performAuthGet(
        url: String,
        account: TestAccount? = null,
    ) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.get(url), account)
            .accept(MediaType.APPLICATION_JSON),
    ).andDo(MockMvcResultHandlers.print())

    fun performAuthPost(
        url: String,
        body: Any,
        account: TestAccount? = null,
    ) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.post(url), account)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(body)),
    ).andDo(MockMvcResultHandlers.print())

    fun performAuthPut(
        url: String,
        body: Any,
        account: TestAccount? = null,
    ) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.put(url), account)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(body)),
    ).andDo(MockMvcResultHandlers.print())

    fun performAuthDelete(
        url: String,
        account: TestAccount? = null,
    ) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.delete(url), account)
            .accept(MediaType.APPLICATION_JSON),
    ).andDo(MockMvcResultHandlers.print())

    fun performAuthPatch(
        url: String,
        body: Any,
        account: TestAccount? = null,
    ) = mockMvc.perform(
        addAuthHeader(MockMvcRequestBuilders.patch(url), account)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(body)),
    ).andDo(MockMvcResultHandlers.print())

    private fun addAuthHeader(
        builder: MockHttpServletRequestBuilder,
        account: TestAccount?,
    ): MockHttpServletRequestBuilder {
        val testAccount = account ?: this.fixtureAccount()
        return builder.header(HttpHeaders.AUTHORIZATION, "Bearer ${testAccount.token}")
    }

    protected fun fixtureAccount(): TestAccount {
        val uuid = UUID.randomUUID().toString()
        val email = "test$uuid@test.com"
        val password = "Password1!"
        val name = "test123"

        val response =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        toJson(
                            mapOf(
                                "email" to email,
                                "password" to password,
                                "name" to name,
                            ),
                        ),
                    ),
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        val accountId =
            objectMapper.readTree(response.response.contentAsString)
                .get("id")
                .asText()

        val token = signIn(email, password)
        return TestAccount(
            id = accountId,
            email = email,
            password = password,
            nickname = name,
            token = token,
        )
    }

    private fun signIn(
        email: String,
        password: String,
    ): String {
        val signInResult =
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/auth/signin")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(
                        toJson(
                            mapOf(
                                "email" to email,
                                "password" to password,
                            ),
                        ),
                    ),
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        val response = objectMapper.readValue(signInResult.response.contentAsString, SignInResponse::class.java)
        return response.token
    }

    protected fun fixtureNovelRoom(account: TestAccount?): String {
        val request =
            mapOf(
                "maxContributors" to 5,
                "title" to "테스트 소설",
                "description" to "테스트 소설 설명",
                "category" to "GENERAL",
                "tags" to listOf("태그1", "태그2"),
                "synopsis" to "테스트 소설의 줄거리입니다.",
                "coverImage" to "https://example.com/cover",
            )

        val response = performAuthPost("/api/v1/novel-rooms", request, account)
        val responseMap = objectMapper.readValue(response.andReturn().response.contentAsString, Map::class.java)
        return responseMap["id"] as String
    }
}
