package lab.ujumeonji.literaturebackend.domain.novel.command

data class UpsertNovelCharacterCommand(
    val id: String? = null,
    val name: String,
    val description: String?,
)
