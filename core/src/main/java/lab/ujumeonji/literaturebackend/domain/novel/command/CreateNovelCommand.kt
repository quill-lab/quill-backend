package lab.ujumeonji.literaturebackend.domain.novel.command

import lab.ujumeonji.literaturebackend.domain.novel.NovelCategory

data class CreateNovelCommand(
    val title: String,
    val category: NovelCategory,
    val coverImage: String?,
    val tags: List<String>,
    val synopsis: String?,
)
