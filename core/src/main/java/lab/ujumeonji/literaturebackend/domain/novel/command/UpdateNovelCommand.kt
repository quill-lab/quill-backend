package lab.ujumeonji.literaturebackend.domain.novel.command

import lab.ujumeonji.literaturebackend.domain.novel.NovelCategory

data class UpdateNovelCommand(
    val title: String?,
    val category: NovelCategory?,
    val tags: List<String>?,
    val synopsis: String?,
)
