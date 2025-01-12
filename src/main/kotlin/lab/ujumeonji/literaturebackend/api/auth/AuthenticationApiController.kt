package lab.ujumeonji.literaturebackend.api.auth

import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.auth.dto.SignInBodyRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.SignInResponse
import lab.ujumeonji.literaturebackend.api.auth.dto.SignUpBodyRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.SignUpResponse
import lab.ujumeonji.literaturebackend.usecase.auth.SignInUseCase
import lab.ujumeonji.literaturebackend.usecase.auth.SignUpUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1")
class AuthenticationApiController(
    private val signInUseCase: SignInUseCase,
    private val signUpUseCase: SignUpUseCase,
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpBodyRequest): SignUpResponse {
        val result = signUpUseCase.execute(
            request = SignUpUseCase.Request(
                email = request.email,
                password = request.password,
                nickname = request.nickname,
            ),
            executedAt = LocalDateTime.now()
        )

        return SignUpResponse(id = result.id)
    }

    @PostMapping("/sign-in")
    fun signIn(@Valid @RequestBody request: SignInBodyRequest): SignInResponse {
        val result = signInUseCase.execute(
            request = SignInUseCase.Request(
                email = request.email,
                password = request.password,
            ),
            executedAt = LocalDateTime.now()
        )

        return SignInResponse(accessToken = result.token)
    }
}
