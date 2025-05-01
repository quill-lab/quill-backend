package lab.ujumeonji.literaturebackend.api.novel.dto

import jakarta.validation.constraints.NotBlank

data class CreateChapterRequest(
    @field:NotBlank(message = "Title is required")
    val title: String,
)
