package lab.ujumeonji.literaturebackend.service.domain.novel.command

data class CreateNovelCommand(
    val name: String,
    val description: String,
    val coverImage: String,
)
