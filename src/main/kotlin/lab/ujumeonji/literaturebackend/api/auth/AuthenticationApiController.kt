package lab.ujumeonji.literaturebackend.api.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.auth.dto.SignInBodyRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.SignInResponse
import lab.ujumeonji.literaturebackend.api.auth.dto.SignUpBodyRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.SignUpResponse
import lab.ujumeonji.literaturebackend.usecase.auth.SignInUseCase
import lab.ujumeonji.literaturebackend.usecase.auth.SignUpUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Tag(name = "Authentication", description = "인증 API")
@RestController
@RequestMapping("/api/v1")
class AuthenticationApiController(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
) {

    @Operation(summary = "회원 가입", description = "회원 가입을 진행합니다.")
    @PostMapping("/sign-up")
    fun signUp(@Valid @RequestBody request: SignUpBodyRequest): ResponseEntity<SignUpResponse> {
        val result = signUpUseCase.execute(
            request = SignUpUseCase.Request(
                email = request.email,
                password = request.password,
                nickname = request.nickname,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(SignUpResponse(id = result.id))
    }

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @PostMapping("/sign-in")
    fun signIn(@Valid @RequestBody request: SignInBodyRequest): ResponseEntity<SignInResponse> {
        val result = signInUseCase.execute(
            request = SignInUseCase.Request(
                email = request.email,
                password = request.password,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(SignInResponse(accessToken = result.token))
    }
}
