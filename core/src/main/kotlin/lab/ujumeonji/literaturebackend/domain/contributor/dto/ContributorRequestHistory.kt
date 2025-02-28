package lab.ujumeonji.literaturebackend.domain.contributor.dto

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRequestStatus
import java.time.LocalDateTime
import java.util.*

data class ContributorRequestHistory(
    val id: UUID,
    val title: String,
    val status: ContributorRequestStatus,
    val requestedAt: LocalDateTime,
    val joinedAt: LocalDateTime?,
    val leftAt: LocalDateTime?,
)
