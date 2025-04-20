package lab.ujumeonji.literaturebackend.api.auth.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CheckNameDuplicationRequest(
    @field:NotBlank(message = "닉네임은 필수입니다")
    @field:Size(min = 2, max = 20, message = "닉네임은 2자 이상 20자 이하여야 합니다")
    val name: String,
)
