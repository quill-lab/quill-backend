package lab.ujumeonji.literaturebackend.api.auth

import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.auth.dto.SignInBodyRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.SignUpBodyRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.SignUpResponse
import lab.ujumeonji.literaturebackend.usecase.auth.SignInUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1")
class AuthenticationApiController(
    private val signInUseCase: SignInUseCase,
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpBodyRequest): SignUpResponse {
        return SignUpResponse("success")
    }

    @PostMapping("/sign-in")
    fun signIn(@Valid @RequestBody request: SignInBodyRequest): SignUpResponse {
        val result = signInUseCase.execute(
            SignInUseCase.Request(
                email = request.email,
                password = request.password,
                executedAt = LocalDateTime.now()
            )
        )

        return SignUpResponse(accessToken = result.token)
    }
}
