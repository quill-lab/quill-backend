package lab.ujumeonji.literaturebackend.api.novel.dto

import jakarta.validation.constraints.*
import lab.ujumeonji.literaturebackend.domain.novel.command.NovelCategoryEnum

data class CreateNovelRoomBodyRequest(

    @field:Min(value = 1, message = "최소 1명 이상의 기여자가 필요합니다")
    @field:Max(value = 100, message = "최대 100명까지만 참여할 수 있습니다")
    val maxContributors: Int,

    @field:NotBlank(message = "제목은 필수입니다")
    @field:Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다")
    val title: String,

    @field:NotBlank(message = "소개는 필수입니다")
    @field:Size(min = 1, max = 200, message = "소개는 1자 이상 200자 이하여야 합니다")
    val description: String,

    @field:NotNull(message = "카테고리는 필수입니다")
    val category: NovelCategoryEnum,

    @field:Size(max = 5, message = "태그는 최대 5개까지만 등록할 수 있습니다")
    val tags: List<String> = emptyList(),

    @field:Size(max = 1000, message = "줄거리는 1000자 이하여야 합니다")
    val synopsis: String? = null,

    @field:Size(max = 200, message = "커버 이미지 URL은 200자 이하여야 합니다")
    val coverImage: String? = null,
)
