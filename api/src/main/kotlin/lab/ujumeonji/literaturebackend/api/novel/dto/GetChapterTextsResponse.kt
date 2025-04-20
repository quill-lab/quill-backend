package lab.ujumeonji.literaturebackend.api.novel.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class GetChapterTextsResponse(
    @Schema(description = "챕터 텍스트 목록")
    val items: List<ChapterText>,
) {
    data class ChapterText(
        @Schema(description = "챕터 텍스트 ID")
        val id: String,
        @Schema(description = "챕터 텍스트 내용")
        val content: String,
        @Schema(description = "작성자 이름")
        val authorName: String,
        @Schema(description = "생성일")
        val createdAt: LocalDateTime,
    )
}
