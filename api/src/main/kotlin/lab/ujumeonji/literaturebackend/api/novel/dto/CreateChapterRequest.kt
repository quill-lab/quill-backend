package lab.ujumeonji.literaturebackend.api.novel.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class CreateChapterRequest(
    @field:NotBlank(message = "챕터 제목은 필수입니다")
    val title: String,
    
    @field:NotNull(message = "챕터 번호는 필수입니다")
    @field:Positive(message = "챕터 번호는 양수여야 합니다")
    val chapterNumber: Int,
    
    val description: String? = null
) 