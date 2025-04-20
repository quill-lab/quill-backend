package lab.ujumeonji.literaturebackend.api.novel.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import lab.ujumeonji.literaturebackend.domain.novel.command.NovelCategoryEnum

data class UpdateNovelRequest(
    @field:NotBlank(message = "제목은 필수입니다")
    @field:Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다")
    val title: String,
    @field:NotBlank(message = "한 줄 설명은 필수입니다")
    @field:Size(max = 500, message = "한 줄 설명은 500자 이하여야 합니다")
    val description: String,
    @field:NotEmpty(message = "태그는 최소 1개 이상이어야 합니다")
    @field:Size(max = 10, message = "태그는 최대 10개까지 가능합니다")
    val tags: List<String>,
    @field:NotNull(message = "카테고리는 필수입니다")
    val category: NovelCategoryEnum,
    @field:NotBlank(message = "줄거리는 필수입니다")
    @field:Size(max = 1000, message = "줄거리는 1000자 이하여야 합니다")
    val synopsis: String,
)
