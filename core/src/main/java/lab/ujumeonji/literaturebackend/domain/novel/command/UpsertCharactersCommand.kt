package lab.ujumeonji.literaturebackend.domain.novel.command

import lab.ujumeonji.literaturebackend.domain.novel.CharacterId

data class UpsertCharactersCommand(
    val id: CharacterId?,
    val name: String,
    val description: String?,
)
