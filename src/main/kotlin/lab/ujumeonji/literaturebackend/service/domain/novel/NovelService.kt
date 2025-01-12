package lab.ujumeonji.literaturebackend.service.domain.novel

import lab.ujumeonji.literaturebackend.domain.novel.Novel
import lab.ujumeonji.literaturebackend.service.domain.novel.command.CreateNovelCommand
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class NovelService(
    private val novelRepository: NovelRepository
) {

    fun createNovel(command: CreateNovelCommand, now: LocalDateTime = LocalDateTime.now()): Novel =
        novelRepository.save(
            Novel.create(
                command.name,
                command.description,
                command.coverImage,
                now,
            )
        )

    fun findNovel(novelId: Long): Novel? =
        novelRepository.findOneById(novelId)
}
