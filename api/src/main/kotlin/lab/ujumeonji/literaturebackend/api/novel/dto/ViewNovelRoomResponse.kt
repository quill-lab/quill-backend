package lab.ujumeonji.literaturebackend.api.novel.dto

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupStatus
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRole
import java.time.LocalDateTime

data class ViewNovelRoomResponse(
    val id: String,
    val category: Category,
    val title: String,
    val description: String,
    val synopsis: String?,
    val createdAt: LocalDateTime,
    val completedAt: LocalDateTime?,
    val role: ContributorRole,
    val contributorCount: Int,
    val maxContributorCount: Int,
    val author: Author?,
    val status: ContributorGroupStatus,
    val tags: List<String>,
) {
    data class Category(
        val name: String,
        val alias: String,
    )

    data class Author(
        val id: String,
        val name: String,
    )
}
