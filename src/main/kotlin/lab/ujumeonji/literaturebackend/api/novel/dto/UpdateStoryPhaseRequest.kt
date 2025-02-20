package lab.ujumeonji.literaturebackend.api.novel.dto

import jakarta.validation.constraints.NotNull
import lab.ujumeonji.literaturebackend.domain.novel.StoryPhase

data class UpdateStoryPhaseRequest(
    @field:NotNull(message = "스토리 페이즈는 필수입니다")
    val phase: StoryPhase,
    val description: String?,
)
