package lab.ujumeonji.literaturebackend.api.novel.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min

data class JoinedNovelRoomsQueryRequest(
    @field:Min(value = 0, message = "페이지는 0 이상이어야 합니다")
    val page: Int = 0,

    @field:Min(value = 1, message = "사이즈는 1 이상이어야 합니다")
    @field:Max(value = 100, message = "사이즈는 100 이하여야 합니다")
    val size: Int = 20,
)
