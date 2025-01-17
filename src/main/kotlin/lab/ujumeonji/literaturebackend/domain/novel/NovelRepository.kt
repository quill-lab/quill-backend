package lab.ujumeonji.literaturebackend.domain.novel

import org.springframework.data.repository.CrudRepository

interface NovelRepository : CrudRepository<Novel, Long> {

    fun findOneById(id: Long): Novel?
}
