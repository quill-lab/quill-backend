package lab.ujumeonji.literaturebackend.api.auth

import com.fasterxml.jackson.databind.ObjectMapper
import lab.ujumeonji.literaturebackend.api.auth.dto.SignInBodyRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.SignUpBodyRequest
import lab.ujumeonji.literaturebackend.support.ControllerTest
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
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
        given("사용자가 회원 정보를 입력 후") {
            val request = SignUpBodyRequest(
                email = "test@example.com",
                password = "password123456789012345",
                nickname = "testuser"
            )

            `when`("회원 가입을 요청하면") {
                val response = performPost("/api/v1/sign-up", request)

                then("신규 회원 정보가 등록된다.") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").exists())
                }
            }
        }

        given("사용자가 이미 가입된 회원 정보가 존재하고") {
            val initialRequest = SignUpBodyRequest(
                email = "test@example.com",
                password = "password123456789012345",
                nickname = "testuser"
            )
            performPost("/api/v1/sign-up", initialRequest)

            val duplicateRequest = SignUpBodyRequest(
                email = initialRequest.email,
                password = "anotherpassword123456789012345",
                nickname = "anotheruser"
            )

            `when`("중복된 이메일로 회원 가입을 요청하면") {
                val duplicateResponse = performPost("/api/v1/sign-up", duplicateRequest)

                then("신규 회원 정보가 등록되지 않는다.") {
                    duplicateResponse
                        .andExpect(status().isConflict)
                        .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_EMAIL.code))
                }
            }
        }

        given("사용자가 로그인 정보를 입력하고") {
            val signUpRequest = SignUpBodyRequest(
                email = "test@example.com",
                password = "password123456789012345",
                nickname = "testuser"
            )
            performPost("/api/v1/sign-up", signUpRequest)

            val signInRequest = SignInBodyRequest(
                email = "test@example.com",
                password = "password123456789012345"
            )

            `when`("로그인을 요청하면") {
                val response = performPost("/api/v1/sign-in", signInRequest)

                then("JWT 토큰이 발급된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.accessToken").exists())
                }
            }
        }

        given("사용자가 잘못된 로그인 정보를 입력하고") {
            val signInRequest = SignInBodyRequest(
                email = "wrong@example.com",
                password = "wrongpassword123456789012345"
            )

            `when`("로그인을 요청하면") {
                val response = performPost("/api/v1/sign-in", signInRequest)

                then("인증 실패 응답이 반환된다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_CREDENTIALS.code))
                }
            }
        }
    }
}
