package lab.ujumeonji.literaturebackend.api.novel.dto

import lab.ujumeonji.literaturebackend.domain.contributor.command.ContributorGroupStatusEnum
import lab.ujumeonji.literaturebackend.domain.contributor.command.ContributorRoleEnum
import java.time.LocalDateTime

data class ViewNovelRoomResponse(
    val id: String,
    val category: Category,
    val title: String,
    val description: String,
    val synopsis: String?,
    val createdAt: LocalDateTime,
    val completedAt: LocalDateTime?,
    val role: ContributorRoleEnum,
    val contributorCount: Int,
    val maxContributorCount: Int,
    val status: ContributorGroupStatusEnum,
    val tags: List<String>,
) {
    data class Category(
        val name: String,
        val alias: String,
    )
}
