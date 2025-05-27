package lab.ujumeonji.literaturebackend.api.novel.dto

import lab.ujumeonji.literaturebackend.domain.novel.command.ChapterPublicationRequestStatusEnum
import java.time.LocalDateTime

data class GetChapterPublicationRequestsResponse(
    val requests: List<ChapterPublicationRequest>,
) {
    data class ChapterPublicationRequest(
        val id: String,
        val chapterId: String,
        val requesterId: String,
        val status: ChapterPublicationRequestStatusEnum,
        val reviewerId: String?,
        val comment: String?,
        val reviewedAt: LocalDateTime?,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
    )
}
