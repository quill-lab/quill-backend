package lab.ujumeonji.literaturebackend.domain.novel.command

data class UpsertCharactersCommand(
    val name: String,
    val description: String?,
)
