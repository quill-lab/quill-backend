package lab.ujumeonji.literaturebackend.domain.novel.command

import lab.ujumeonji.literaturebackend.domain.novel.NovelCategory

data class CreateNovelCommand(
    val title: String,
    val category: NovelCategory,
    val tags: List<String>,
    val summary: String,
    val intention: String,
    val background: String?,
    val synopsis: String?,
    val coverImage: String?,
)
