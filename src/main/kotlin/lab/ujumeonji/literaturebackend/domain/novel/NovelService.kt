package lab.ujumeonji.literaturebackend.domain.novel

import lab.ujumeonji.literaturebackend.domain.novel.command.AddCharacterCommand
import lab.ujumeonji.literaturebackend.domain.novel.command.CreateNovelCommand
import lab.ujumeonji.literaturebackend.domain.novel.command.UpdateNovelCommand
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
                command.coverImage,
                command.tags,
                command.synopsis,
                now,
            )
        )

    fun findNovel(id: Long): Novel? = novelRepository.findOneById(id)

    fun findNovels(ids: List<Long>): List<Novel> = novelRepository.findAllById(ids).toList()

    fun findById(id: Long): Novel? = novelRepository.findById(id).orElse(null)

    @Transactional
    fun update(id: Long, command: UpdateNovelCommand, now: LocalDateTime = LocalDateTime.now()): Novel {
        val novel = findById(id) ?: throw IllegalArgumentException("Novel not found")

        novel.updateBasicInfo(
            command.title,
            command.description,
            command.synopsis,
            command.category,
            now,
        )

        novel.updateTags(
            command.tags,
            now,
        )

        return novelRepository.save(novel)
    }

    @Transactional
    fun addCharacter(novelId: Long, command: AddCharacterCommand, now: LocalDateTime = LocalDateTime.now()): Long =
        findById(novelId)
            ?.apply { addCharacter(command, now) }
            ?.let { novelRepository.save(it) }?.characters?.last()?.id
            ?: throw IllegalArgumentException("Novel not found")
}
