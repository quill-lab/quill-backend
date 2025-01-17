package lab.ujumeonji.literaturebackend.domain.novel

import lab.ujumeonji.literaturebackend.domain.novel.command.CreateNovelCommand
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
                command.tags,
                command.synopsis,
                now,
            )
        )

    fun findNovel(id: Long): Novel? = novelRepository.findOneById(id)

    fun findNovels(ids: List<Long>): List<Novel> = novelRepository.findAllById(ids).toList()

    fun findById(id: Long): Novel? = novelRepository.findById(id).orElse(null)
}
