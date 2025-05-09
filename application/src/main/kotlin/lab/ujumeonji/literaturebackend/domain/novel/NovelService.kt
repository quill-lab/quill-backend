package lab.ujumeonji.literaturebackend.domain.novel

import lab.ujumeonji.literaturebackend.domain.common.dto.PageResponse
import lab.ujumeonji.literaturebackend.domain.novel.command.CreateNovelCommand
import lab.ujumeonji.literaturebackend.domain.novel.dto.ChapterData
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional(readOnly = true)
class NovelService(
    private val novelRepository: NovelRepository,
    private val chapterRepository: ChapterRepository,
) {
    @Transactional
    fun createNovel(
        command: CreateNovelCommand,
        now: LocalDateTime = LocalDateTime.now(),
    ): Novel =
        novelRepository.save(
            Novel.create(
                command.title,
                command.description,
                command.category,
                command.coverImage,
                command.tags,
                command.synopsis,
                now,
            ),
        )

    fun findNovel(id: NovelId): Novel? = novelRepository.findOneById(id.id)

    fun findNovels(ids: List<NovelId>): List<Novel> = novelRepository.findAllById(ids.map { it.id }).toList()

    fun findChapterById(id: ChapterId): ChapterData? {
        val chapterOptional = chapterRepository.findById(id.id)
        if (chapterOptional.isEmpty) {
            return null
        }

        val chapter = chapterOptional.get()
        return ChapterData(
            id = chapter.idValue,
            title = chapter.title,
            description = chapter.description,
            chapterNumber = chapter.chapterNumber,
            status = chapter.status,
            currentChapterInfo = chapter.currentAuthor.getOrNull(),
            approvedAt = chapter.approvedAt,
            createdAt = chapter.createdAt,
            updatedAt = chapter.updatedAt,
        )
    }

    fun findChaptersByNovelId(
        novelId: NovelId,
        offset: Int,
        limit: Int,
    ): PageResponse<ChapterData> {
        val pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.ASC, "chapterNumber"))
        val chaptersPage = chapterRepository.findByNovelId(novelId.id, pageable)

        val chapters =
            chaptersPage.content.map { chapter ->
                ChapterData(
                    id = chapter.idValue,
                    title = chapter.title,
                    description = chapter.description,
                    chapterNumber = chapter.chapterNumber,
                    status = chapter.status,
                    currentChapterInfo = chapter.currentAuthor.getOrNull(),
                    approvedAt = chapter.approvedAt,
                    createdAt = chapter.createdAt,
                    updatedAt = chapter.updatedAt,
                )
            }

        return PageResponse(
            items = chapters,
            totalCount = chaptersPage.totalElements.toInt(),
            offset = offset,
            limit = limit,
            hasNext = chaptersPage.hasNext(),
            hasPrevious = chaptersPage.hasPrevious(),
        )
    }

    fun findEpisodesByNovelId(
        novelId: NovelId,
        offset: Int,
        limit: Int,
    ): PageResponse<ChapterData> {
        val pageable = PageRequest.of(offset / limit, limit, Sort.by(Sort.Direction.DESC, "chapterNumber"))
        val chaptersPage = chapterRepository.findByNovelIdAndStatusIs(novelId.id, ChapterStatus.APPROVED, pageable)

        val chapters =
            chaptersPage.content.map { chapter ->
                ChapterData(
                    id = chapter.idValue,
                    title = chapter.title,
                    description = chapter.description,
                    chapterNumber = chapter.chapterNumber,
                    status = chapter.status,
                    currentChapterInfo = chapter.currentAuthor.getOrNull(),
                    approvedAt = chapter.approvedAt,
                    createdAt = chapter.createdAt,
                    updatedAt = chapter.updatedAt,
                )
            }

        return PageResponse(
            items = chapters,
            totalCount = chaptersPage.totalElements.toInt(),
            offset = offset,
            limit = limit,
            hasNext = chaptersPage.hasNext(),
            hasPrevious = chaptersPage.hasPrevious(),
        )
    }
}
