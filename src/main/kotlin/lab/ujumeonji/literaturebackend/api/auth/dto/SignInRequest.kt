package lab.ujumeonji.literaturebackend.api.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class SignInRequest(
    @field:Email(message = "Invalid email format")
    @field:NotBlank(message = "Email is mandatory")
    val email: String,

    @field:NotBlank(message = "Password is mandatory")
    val password: String
)
