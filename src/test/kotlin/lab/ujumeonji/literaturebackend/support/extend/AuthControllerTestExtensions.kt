package lab.ujumeonji.literaturebackend.support.extend

import lab.ujumeonji.literaturebackend.api.auth.dto.SignInResponse
import lab.ujumeonji.literaturebackend.support.AuthControllerTest
import lab.ujumeonji.literaturebackend.support.TestAccount
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.util.*

fun AuthControllerTest.fixtureAccount(): TestAccount {
    val uuid = UUID.randomUUID().toString()
    val email = "test$uuid@test.com"
    val password = "Password1!"
    val nickname = "test$uuid"

    val signUpRequest = mapOf(
        "email" to email,
        "password" to password,
        "nickname" to nickname
    )

    performPost("/api/v1/auth/sign-up", signUpRequest)
        .andExpect(MockMvcResultMatchers.status().isCreated)

    val accessToken = signIn(email, password)
    return TestAccount(
        email = email,
        password = password,
        nickname = nickname,
        accessToken = accessToken
    )
}

private fun AuthControllerTest.signIn(email: String, password: String): String {
    val signInRequest = mapOf(
        "email" to email,
        "password" to password
    )

    val signInResult = performPost("/api/v1/auth/sign-in", signInRequest)
        .andExpect(MockMvcResultMatchers.status().isOk)
        .andReturn()

    val response = objectMapper.readValue(signInResult.response.contentAsString, SignInResponse::class.java)
    return response.accessToken
}

fun AuthControllerTest.fixtureNovelRoom(account: TestAccount? = null): Long {
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
