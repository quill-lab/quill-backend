package lab.ujumeonji.literaturebackend.domain.novel.command

data class CreateNovelCommand(
    val title: String,
    val description: String,
    val coverImage: String?,
    val tags: List<String>,
    val synopsis: String?,
)
