package lab.ujumeonji.literaturebackend.api.novel.dto

data class NovelCharacterResponse(
    val id: Long,
    val name: String,
    val description: String,
    val profileImage: String,
)
