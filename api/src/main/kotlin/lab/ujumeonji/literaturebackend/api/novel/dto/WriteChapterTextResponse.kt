package lab.ujumeonji.literaturebackend.api.novel.dto

import io.swagger.v3.oas.annotations.media.Schema

data class WriteChapterTextResponse(
    @Schema(description = "생성된 챕터 텍스트 ID")
    val id: String
)
