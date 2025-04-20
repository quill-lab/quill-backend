package lab.ujumeonji.literaturebackend.api.novel.dto

data class UpsertNovelCharactersResponse(
    val characters: List<CharacterResponse>,
) {
    data class CharacterResponse(
        val id: String,
    )
}
