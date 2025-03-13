package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.novel.NovelId
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.domain.novel.command.ChapterStatusEnum
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class FindChaptersUseCase(
    private val novelService: NovelService,
) : UseCase<FindChaptersUseCase.Request, FindChaptersUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        novelService.findNovel(NovelId.from(request.novelId))
            ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        val pageResponse = novelService.findChaptersByNovelId(
            novelId = NovelId.from(request.novelId),
            offset = request.offset,
            limit = request.limit
        )

        return Response(
            chapters = pageResponse.items.map { chapter ->
                Response.ChapterItem(
                    id = chapter.id.toString(),
                    episode = chapter.chapterNumber,
                    title = chapter.title,
                    editedAt = chapter.updatedAt,
                    status = ChapterStatusEnum.fromChapterStatus(chapter.status),
                    approvedAt = chapter.approvedAt,
                    metadata = Response.ChapterItem.ChapterMetadata(
                        viewCount = 0,
                        commentCount = 0,
                        likeCount = 0,
                    )
                )
            },
            totalCount = pageResponse.totalCount,
            offset = request.offset,
            limit = request.limit
        )
    }

    data class Request(
        val novelId: String,
        val offset: Int = 0,
        val limit: Int = 20
    )

    data class Response(
        val chapters: List<ChapterItem>,
        val totalCount: Int,
        val offset: Int,
        val limit: Int
    ) {

        data class ChapterItem(
            val id: String,
            val episode: Int,
            val title: String,
            val editedAt: LocalDateTime,
            val status: ChapterStatusEnum,
            val approvedAt: LocalDateTime?,
            val metadata: ChapterMetadata
        ) {

            data class ChapterMetadata(
                val viewCount: Int,
                val commentCount: Int,
                val likeCount: Int
            )
        }
    }
}
