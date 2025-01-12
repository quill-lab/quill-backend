package lab.ujumeonji.literaturebackend.service.domain.novel

import lab.ujumeonji.literaturebackend.domain.novel.Novel
import org.springframework.data.repository.CrudRepository

interface NovelRepository : CrudRepository<Novel, Long> {
}
