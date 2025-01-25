package lab.ujumeonji.literaturebackend.api.novel.dto

import jakarta.validation.constraints.Min

data class UpdateParticipantOrderRequest(
    @field:Min(0, message = "순서는 0 이상이어야 합니다.")
    val writingOrder: Int,
)
