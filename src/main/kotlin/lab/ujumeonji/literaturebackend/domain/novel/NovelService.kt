package lab.ujumeonji.literaturebackend.domain.novel

import lab.ujumeonji.literaturebackend.domain.novel.command.CreateNovelCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class NovelService(
    private val novelRepository: NovelRepository
) {

    @Transactional
    fun createNovel(command: CreateNovelCommand, now: LocalDateTime = LocalDateTime.now()): Novel =
        novelRepository.save(
            Novel.create(
                command.title,
                command.description,
                command.category,
                command.coverImage,
                command.tags,
                command.synopsis,
                now,
            )
        )

    fun findNovel(id: NovelId): Novel? = novelRepository.findOneById(id.id)

    fun findNovels(ids: List<NovelId>): List<Novel> = novelRepository.findAllById(ids.map { it.id }).toList()
}
