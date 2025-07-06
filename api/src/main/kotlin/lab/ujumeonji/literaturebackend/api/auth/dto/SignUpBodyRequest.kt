package lab.ujumeonji.literaturebackend.api.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SignUpBodyRequest(
    @field:Email(message = "올바른 이메일 형식이어야 합니다")
    @field:NotBlank(message = "이메일은 필수입니다")
    val email: String,
    @field:NotBlank(message = "비밀번호는 필수입니다")
    @field:Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다")
    @field:Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#\$%^&*()\\-_=+{}\\[\\]|;:'\",.<>?`~]).{8,20}$",
        message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다",
    )
    val password: String,
    @field:NotBlank(message = "닉네임은 필수입니다")
    @field:Size(min = 2, max = 10, message = "닉네임은 2자 이상 10자 이하여야 합니다")
    val name: String,
)
