package lab.ujumeonji.literaturebackend.api.novel.dto

import lab.ujumeonji.literaturebackend.domain.novel.command.StoryPhaseEnum
import java.time.LocalDateTime

data class StoryArcResponse(
    val id: String,
    val description: String?,
    val phase: StoryPhaseEnum,
    val phaseAlias: String,
    val firstChapterNumber: Int?,
    val lastChapterNumber: Int?,
    val lastModifiedAt: LocalDateTime?,
)
