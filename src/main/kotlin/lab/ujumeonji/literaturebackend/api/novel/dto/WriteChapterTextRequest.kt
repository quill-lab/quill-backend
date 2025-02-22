package lab.ujumeonji.literaturebackend.api.novel.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class WriteChapterTextRequest(
    @field:NotBlank(message = "Content must not be blank")
    @field:Size(min = 30, message = "Content must be at least 30 characters")
    @Schema(description = "챕터 텍스트 내용", example = "이것은 챕터의 내용입니다. 최소 30자 이상의 텍스트가 필요합니다...")
    val content: String
)
