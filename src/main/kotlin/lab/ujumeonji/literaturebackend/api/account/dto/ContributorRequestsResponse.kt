package lab.ujumeonji.literaturebackend.api.account.dto

import java.time.LocalDateTime

data class ContributorRequestsResponse(
    val items: List<ResponseItem>,
    val totalCount: Long,
    val size: Int,
    val page: Int,
) {
    data class ResponseItem(
        val id: String,
        val title: String,
        val requestedAt: LocalDateTime,
        val joinedAt: LocalDateTime?,
        val leftAt: LocalDateTime?,
        val status: String,
    )
}
