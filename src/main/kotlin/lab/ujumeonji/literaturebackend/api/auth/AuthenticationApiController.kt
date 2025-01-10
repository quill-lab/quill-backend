package lab.ujumeonji.literaturebackend.api.auth

import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.auth.dto.SignInRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.SignUpRequest
import lab.ujumeonji.literaturebackend.api.auth.dto.SignUpResponse
import lab.ujumeonji.literaturebackend.usecase.auth.SignInUseCase
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationApiController(
    private val signInUseCase: SignInUseCase,
) {

    @PostMapping("/sign-up")
    fun signUp(@RequestBody request: SignUpRequest): SignUpResponse {
        return SignUpResponse("success")
    }

    @PostMapping("/sign-in")
    fun signIn(@Valid @RequestBody request: SignInRequest): SignUpResponse {
        val result = signInUseCase.execute(SignInUseCase.Request(email = request.email, password = request.password))

        return SignUpResponse(accessToken = result.token)
    }
}
