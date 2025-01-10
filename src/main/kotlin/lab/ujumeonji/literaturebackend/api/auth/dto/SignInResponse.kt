package lab.ujumeonji.literaturebackend.api.auth.dto

import jakarta.validation.constraints.NotBlank

data class SignInResponse(
    @field:NotBlank(message = "Token is mandatory")
    val accessToken: String
)
