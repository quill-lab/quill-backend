package lab.ujumeonji.literaturebackend.domain.novel.dto

import lab.ujumeonji.literaturebackend.domain.novel.ChapterPublicationRequest
import lab.ujumeonji.literaturebackend.domain.novel.command.ChapterPublicationRequestStatusEnum
import java.time.LocalDateTime

data class ChapterPublicationRequestDto(
    val id: String,
    val chapterId: String,
    val requesterId: String,
    val status: ChapterPublicationRequestStatusEnum,
    val reviewerId: String?,
    val comment: String?,
    val reviewedAt: LocalDateTime?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun from(request: ChapterPublicationRequest): ChapterPublicationRequestDto {
            return ChapterPublicationRequestDto(
                id = request.idValue.toString(),
                chapterId = request.chapterId.toString(),
                requesterId = request.requesterId.toString(),
                status = ChapterPublicationRequestStatusEnum.fromChapterPublicationRequestStatus(request.status),
                reviewerId = request.reviewerId?.toString(),
                comment = request.comment,
                reviewedAt = request.reviewedAt,
                createdAt = request.createdAt,
                updatedAt = request.updatedAt,
            )
        }
    }
}
