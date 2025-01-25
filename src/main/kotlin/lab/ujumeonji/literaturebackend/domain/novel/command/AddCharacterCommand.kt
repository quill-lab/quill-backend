package lab.ujumeonji.literaturebackend.domain.novel.command

data class AddCharacterCommand(
    val name: String,
    val description: String,
)
