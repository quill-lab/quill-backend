package lab.ujumeonji.literaturebackend.api.auth

import com.fasterxml.jackson.databind.ObjectMapper
import lab.ujumeonji.literaturebackend.api.auth.dto.SignInBodyRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.SignUpBodyRequest
import lab.ujumeonji.literaturebackend.support.ControllerTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthenticationApiControllerTest(
    mockMvc: MockMvc,
    objectMapper: ObjectMapper,
) : ControllerTest(
    mockMvc = mockMvc,
    objectMapper = objectMapper,
) {

    init {
        given("회원가입 API 호출 시") {
            val request = SignUpBodyRequest(
                email = "test@example.com",
                password = "password123",
                nickname = "testuser"
            )

            `when`("유효한 요청을 보내면") {
                val response = performPost("/api/v1/sign-up", request)

                then("회원가입이 성공해야 한다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").exists())
                }
            }

            `when`("이미 존재하는 이메일로 요청하면") {
                val response = performPost("/api/v1/sign-up", request)

                then("409 Conflict 응답이 반환되어야 한다") {
                    response.andExpect(status().isConflict)
                }
            }
        }

        given("로그인 API 호출 시") {
            val request = SignInBodyRequest(
                email = "test@example.com",
                password = "password123"
            )

            `when`("올바른 인증 정보로 요청하면") {
                val response = performPost("/api/v1/sign-in", request)

                then("로그인이 성공하고 토큰이 반환되어야 한다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.accessToken").exists())
                }
            }

            `when`("잘못된 인증 정보로 요청하면") {
                val invalidRequest = request.copy(password = "wrongpassword")
                val response = performPost("/api/v1/sign-in", invalidRequest)

                then("401 Unauthorized 응답이 반환되어야 한다") {
                    response.andExpect(status().isUnauthorized)
                }
            }
        }
    }
}
