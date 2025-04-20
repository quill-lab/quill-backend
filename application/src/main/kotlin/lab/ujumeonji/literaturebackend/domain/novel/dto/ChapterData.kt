package lab.ujumeonji.literaturebackend.domain.novel.dto

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorId
import lab.ujumeonji.literaturebackend.domain.novel.ChapterId
import lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus
import java.time.LocalDateTime

data class ChapterData(
    val id: ChapterId,
    val title: String?,
    val description: String?,
    val chapterNumber: Int,
    val status: ChapterStatus,
    val approvedAt: LocalDateTime?,
    val currentContributorId: ContributorId?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
