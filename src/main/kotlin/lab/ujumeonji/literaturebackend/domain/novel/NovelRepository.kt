package lab.ujumeonji.literaturebackend.domain.novel

import lab.ujumeonji.literaturebackend.domain.novel.Novel
import org.springframework.data.repository.CrudRepository

interface NovelRepository : CrudRepository<Novel, Long> {

    fun findOneById(id: Long): Novel?
}
