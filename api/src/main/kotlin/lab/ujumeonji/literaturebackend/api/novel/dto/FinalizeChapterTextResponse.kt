package lab.ujumeonji.literaturebackend.api.novel.dto

import io.swagger.v3.oas.annotations.media.Schema

data class FinalizeChapterTextResponse(
    @Schema(description = "작업 성공 여부")
    val success: Boolean,
)