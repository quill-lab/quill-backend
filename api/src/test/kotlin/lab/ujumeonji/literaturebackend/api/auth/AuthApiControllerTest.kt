package lab.ujumeonji.literaturebackend.api.auth

import com.fasterxml.jackson.databind.ObjectMapper
import lab.ujumeonji.literaturebackend.api.auth.dto.SignInBodyRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.SignUpBodyRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.TemporaryPasswordRequest
import lab.ujumeonji.literaturebackend.support.ControllerTest
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthApiControllerTest(
    mockMvc: MockMvc,
    objectMapper: ObjectMapper,
) : ControllerTest(
    mockMvc = mockMvc,
    objectMapper = objectMapper,
) {
    init {
        given("사용자가 회원 정보를 입력 후") {
            val request =
                SignUpBodyRequest(
                    email = "test123@example.com",
                    password = "Password123^!@#",
                    name = "testuser1",
                )

            `when`("회원 가입을 요청하면") {
                val response = performPost("/api/v1/auth/signup", request)

                then("신규 회원 정보가 등록된다.") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").exists())
                }
            }
        }

        given("사용자가 이미 가입된 회원 정보가 존재하고") {
            val initialRequest =
                SignUpBodyRequest(
                    email = "test@example.com",
                    password = "Password!@#\$1",
                    name = "testuser",
                )
            performPost("/api/v1/auth/signup", initialRequest)

            val duplicateRequest =
                SignUpBodyRequest(
                    email = initialRequest.email,
                    password = "anotherpa!@#\$1",
                    name = "another",
                )

            `when`("중복된 이메일로 회원 가입을 요청하면") {
                val duplicateResponse = performPost("/api/v1/auth/signup", duplicateRequest)

                then("신규 회원 정보가 등록되지 않는다.") {
                    duplicateResponse
                        .andExpect(status().isConflict)
                        .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_EMAIL.code))
                }
            }
        }

        given("사용자가 로그인 정보를 입력하고") {
            val signUpRequest =
                SignUpBodyRequest(
                    email = "test@example.com",
                    password = "Password!@#\$1",
                    name = "testuser",
                )
            performPost("/api/v1/auth/signup", signUpRequest)

            val signInRequest =
                SignInBodyRequest(
                    email = "test@example.com",
                    password = "Password!@#\$1",
                )

            `when`("로그인을 요청하면") {
                val response = performPost("/api/v1/auth/signin", signInRequest)

                then("JWT 토큰이 발급된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.token").exists())
                }
            }
        }

        given("사용자가 잘못된 로그인 정보를 입력하고") {
            val signInRequest =
                SignInBodyRequest(
                    email = "wrong@example.com",
                    password = "Wrong!@#\$1",
                )

            `when`("로그인을 요청하면") {
                val response = performPost("/api/v1/auth/signin", signInRequest)

                then("인증 실패 응답이 반환된다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.INVALID_CREDENTIALS.code))
                }
            }
        }

        given("사용자가 임시 비밀번호를 요청하고") {
            val signUpRequest =
                SignUpBodyRequest(
                    email = "test@example.com",
                    password = "Password!@#\$1",
                    name = "testuser",
                )
            performPost("/api/v1/auth/signup", signUpRequest)

            val temporaryPasswordRequest =
                TemporaryPasswordRequest(
                    email = "test@example.com",
                )

            `when`("등록된 이메일로 임시 비밀번호를 요청하면") {
                val response = performPost("/api/v1/auth/password/temporary", temporaryPasswordRequest)

                then("임시 비밀번호가 발급된다") {
                    response.andExpect(status().isOk)
                }
            }

            `when`("등록되지 않은 이메일로 임시 비밀번호를 요청하면") {
                val invalidRequest =
                    TemporaryPasswordRequest(
                        email = "nonexistent@example.com",
                    )
                val response = performPost("/api/v1/auth/password/temporary", invalidRequest)

                then("계정을 찾을 수 없다는 응답이 반환된다") {
                    response
                        .andExpect(status().isNotFound)
                        .andExpect(jsonPath("$.code").value(ErrorCode.EMAIL_NOT_FOUND.code))
                }
            }
        }
    }
}
