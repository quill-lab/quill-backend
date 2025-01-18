package lab.ujumeonji.literaturebackend.api.novel.dto

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRole

data class ViewNovelRoomResponse(
    val id: Long,
    val category: Category,
    val title: String,
    val createdAt: String,
    val completedAt: String?,
    val role: ContributorRole,
    val contributorCount: Int,
    val maxContributorCount: Int,
    val author: Author,
    val status: String,
) {
    data class Category(
        val name: String,
        val alias: String,
    )

    data class Author(
        val id: Long,
        val name: String,
    )
}
