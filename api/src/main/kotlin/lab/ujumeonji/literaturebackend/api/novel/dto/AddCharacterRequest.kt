package lab.ujumeonji.literaturebackend.api.novel.dto

import jakarta.validation.constraints.NotBlank

data class AddCharacterRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val description: String,
)
