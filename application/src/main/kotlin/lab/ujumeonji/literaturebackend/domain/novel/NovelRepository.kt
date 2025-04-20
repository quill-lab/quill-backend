package lab.ujumeonji.literaturebackend.domain.novel

import org.springframework.data.repository.CrudRepository
import java.util.*

interface NovelRepository : CrudRepository<Novel, UUID> {
    fun findOneById(id: UUID): Novel?
}
