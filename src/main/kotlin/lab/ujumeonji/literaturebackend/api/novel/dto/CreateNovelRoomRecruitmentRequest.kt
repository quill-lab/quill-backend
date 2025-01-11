package lab.ujumeonji.literaturebackend.api.novel.dto

import jakarta.validation.constraints.*

data class CreateNovelRoomRecruitmentRequest(
    @field:NotBlank(message = "제목은 필수입니다")
    @field:Size(min = 1, max = 100, message = "제목은 1자 이상 100자 이하여야 합니다")
    val title: String,

    @field:NotBlank(message = "내용은 필수입니다") 
    @field:Size(min = 1, max = 1000, message = "내용은 1자 이상 1000자 이하여야 합니다")
    val content: String,

    @field:NotBlank(message = "링크는 필수입니다")
    @field:Size(max = 200, message = "링크는 200자 이하여야 합니다")
    val link: String,
)
