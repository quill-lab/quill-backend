package lab.ujumeonji.literaturebackend.api.novel.dto

import java.time.LocalDateTime

data class NovelCharacterResponse(
    val id: Long,
    val name: String,
    val description: String,
    val profileImage: String,
    val updatedAt: LocalDateTime?,
    val updatedBy: LastCharacterUpdatedBy?,
) {

    data class LastCharacterUpdatedBy(
        val id: Long,
        val name: String,
    )
}
