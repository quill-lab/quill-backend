package lab.ujumeonji.literaturebackend.api.novel.dto

import java.time.LocalDateTime

data class JoinedNovelRoomsResponse(
    val items: List<ResponseItem>,
    val totalCount: Long,
    val size: Int,
    val page: Int,
) {
    data class ResponseItem(
        val id: String,
        val category: Category,
        val title: String,
        val createdAt: LocalDateTime,
        val completedAt: LocalDateTime?,
        val role: String,
        val contributorCount: Int,
        val maxContributorCount: Int,
        val status: String,
    ) {

        data class Category(
            val name: String,
            val alias: String,
        )
    }
}
