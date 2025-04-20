package lab.ujumeonji.literaturebackend.domain.novel

import java.util.*

interface CharacterRepository {
    fun save(character: Character): Character

    fun findOneById(id: String): Character?

    fun findAllByNovelId(novelId: UUID): List<Character>
}
