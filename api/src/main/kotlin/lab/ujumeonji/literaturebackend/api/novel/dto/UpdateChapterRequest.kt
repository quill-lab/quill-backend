package lab.ujumeonji.literaturebackend.api.novel.dto

import jakarta.validation.constraints.Size

data class UpdateChapterRequest(
    @field:Size(max = 255, message = "챕터 제목은 255자 이하여야 합니다")
    val title: String?,
)
