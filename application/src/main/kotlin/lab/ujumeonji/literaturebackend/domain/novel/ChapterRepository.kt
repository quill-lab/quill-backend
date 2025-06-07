package lab.ujumeonji.literaturebackend.domain.novel

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import java.util.*

interface ChapterRepository : CrudRepository<Chapter, UUID> {
    fun findByNovelId(
        novelId: UUID,
        pageable: Pageable,
    ): Page<Chapter>

    fun findByNovelIdAndStatusIs(
        novelId: UUID,
        status: ChapterStatus,
        pageable: Pageable,
    ): Page<Chapter>
}
