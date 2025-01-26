package lab.ujumeonji.literaturebackend.api.novel.dto

import io.swagger.v3.oas.annotations.media.Schema
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRole
import java.time.LocalDateTime

@Schema(description = "소설 공방 참여자 조회 응답")
data class NovelRoomParticipantsResponse(
    @Schema(description = "참여자 ID")
    val id: String,

    @Schema(description = "참여자 닉네임")
    val nickname: String,

    @Schema(description = "참여자 역할")
    val role: ContributorRole,

    @Schema(description = "집필 순서")
    val writingOrder: Int?,

    @Schema(description = "참여일시")
    val joinedAt: LocalDateTime,
)
