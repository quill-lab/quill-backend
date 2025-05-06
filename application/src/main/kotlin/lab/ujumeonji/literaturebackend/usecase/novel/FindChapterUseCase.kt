package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.novel.ChapterId
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
class FindChapterUseCase(
    private val accountService: AccountService,
    private val novelService: NovelService,
) : UseCase<FindChapterUseCase.Request, FindChapterUseCase.Response> {
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val chapterId = ChapterId.from(request.chapterId)
        val chapter = novelService.findChapterById(chapterId)
            ?: throw BusinessException(ErrorCode.CHAPTER_NOT_FOUND)

        val author = chapter.currentChapterInfo

        return Response(
            id = chapter.id.toString(),
            episode = chapter.chapterNumber,
            title = chapter.title,
            editedAt = chapter.updatedAt,
            status = ChapterStatusEnum.fromChapterStatus(chapter.status),
            approvedAt = chapter.approvedAt,
            currentAuthor = author?.let { author ->
                val account = accountService.findById(author.accountId)
                Response.Author(
                    id = author.chapterAuthorId.toString(),
                    accountId = account?.idValue.toString(),
                    name = account?.name,
                )
            },
            metadata = Response.ChapterMetadata(
                viewCount = 0,
                commentCount = 0,
                likeCount = 0,
            ),
        )
    }

    data class Request(
        val chapterId: String,
        val accountId: String,
    )

    data class Response(
        val id: String,
        val episode: Int,
        val title: String?,
        val editedAt: LocalDateTime,
        val status: ChapterStatusEnum,
        val approvedAt: LocalDateTime?,
        val currentAuthor: Author?,
        val metadata: ChapterMetadata,
    ) {
        data class ChapterMetadata(
            val viewCount: Int,
            val commentCount: Int,
            val likeCount: Int,
        )

        data class Author(
            val id: String,
            val accountId: String,
            val name: String?,
        )
    }
}
