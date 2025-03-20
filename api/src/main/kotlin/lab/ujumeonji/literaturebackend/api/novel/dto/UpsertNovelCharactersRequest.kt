package lab.ujumeonji.literaturebackend.api.novel.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UpsertNovelCharactersRequest(
    @field:NotEmpty(message = "등장인물 목록은 비어있을 수 없습니다")
    @field:Valid
    val characters: List<CharacterRequest>
) {
    data class CharacterRequest(
        val id: String?,

        @field:NotNull(message = "등장인물 이름은 필수입니다")
        @field:Size(min = 1, max = 50, message = "등장인물 이름은 1자 이상 50자 이하여야 합니다")
        val name: String,

        @field:Size(max = 1000, message = "등장인물 설명은 1000자 이하여야 합니다")
        val description: String?
    )
} 