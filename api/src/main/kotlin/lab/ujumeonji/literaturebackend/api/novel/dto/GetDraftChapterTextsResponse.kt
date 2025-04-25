package lab.ujumeonji.literaturebackend.api.novel.dto

import java.time.LocalDateTime

data class GetDraftChapterTextsResponse(
    val id: String,
    val content: String,
    val accountId: String,
    val authorName: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
