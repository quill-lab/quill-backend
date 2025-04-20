package lab.ujumeonji.literaturebackend.api.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.auth.dto.*
import lab.ujumeonji.literaturebackend.usecase.auth.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Tag(name = "Authentication", description = "인증 API")
@RestController
@RequestMapping("/api/v1/auth")
class AuthApiController(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
    private val requestTemporaryPasswordUseCase: RequestTemporaryPasswordUseCase,
    private val checkEmailDuplicationUseCase: CheckEmailDuplicationUseCase,
    private val checkNameDuplicationUseCase: CheckNameDuplicationUseCase,
) {
    @Operation(summary = "회원 가입", description = "회원 가입을 진행합니다.")
    @PostMapping("/signup")
    fun signUp(
        @Valid @RequestBody request: SignUpBodyRequest,
    ): ResponseEntity<SignUpResponse> {
        val result =
            signUpUseCase.execute(
                request =
                    SignUpUseCase.Request(
                        email = request.email,
                        password = request.password,
                        name = request.name,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(SignUpResponse(id = result.id))
    }

    @Operation(summary = "로그인", description = "로그인을 진행합니다.")
    @PostMapping("/signin")
    fun signIn(
        @Valid @RequestBody request: SignInBodyRequest,
    ): ResponseEntity<SignInResponse> {
        val result =
            signInUseCase.execute(
                request =
                    SignInUseCase.Request(
                        email = request.email,
                        password = request.password,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(SignInResponse(token = result.token))
    }

    @Operation(summary = "임시 비밀번호 발급", description = "임시 비밀번호를 발급하고 이메일로 전송합니다.")
    @PostMapping("/password/temporary")
    fun requestTemporaryPassword(
        @Valid @RequestBody request: TemporaryPasswordRequest,
    ): ResponseEntity<Unit> {
        requestTemporaryPasswordUseCase.execute(
            request =
                RequestTemporaryPasswordUseCase.Request(
                    email = request.email,
                ),
            executedAt = LocalDateTime.now(),
        )

        return ResponseEntity.ok().build()
    }

    @Operation(
        summary = "이메일 중복 검사",
        description = "회원 가입 시 사용할 이메일의 중복 여부를 검사합니다.",
    )
    @PostMapping("/check-email")
    fun checkEmailDuplication(
        @Valid @RequestBody request: CheckEmailDuplicationRequest,
    ): ResponseEntity<CheckEmailDuplicationResponse> {
        val result =
            checkEmailDuplicationUseCase.execute(
                request =
                    CheckEmailDuplicationUseCase.Request(
                        email = request.email,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(CheckEmailDuplicationResponse(isDuplicated = result.isDuplicated))
    }

    @Operation(
        summary = "닉네임 중복 검사",
        description = "회원 가입 시 사용할 닉네임의 중복 여부를 검사합니다.",
    )
    @PostMapping("/check-nickname")
    fun checkNicknameDuplication(
        @Valid @RequestBody request: CheckNameDuplicationRequest,
    ): ResponseEntity<CheckNameDuplicationResponse> {
        val result =
            checkNameDuplicationUseCase.execute(
                request =
                    CheckNameDuplicationUseCase.Request(
                        name = request.name,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(CheckNameDuplicationResponse(isDuplicated = result.isDuplicated))
    }
}
