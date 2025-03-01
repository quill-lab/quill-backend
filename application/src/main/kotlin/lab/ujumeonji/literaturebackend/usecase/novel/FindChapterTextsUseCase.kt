package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.Account
import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.novel.ChapterId
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class FindChapterTextsUseCase(
    private val novelService: NovelService,
    private val accountService: AccountService,
    private val contributorService: ContributorService,
) : UseCase<FindChapterTextsUseCase.Request, FindChapterTextsUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val accountId = AccountId.from(request.accountId)
        accountService.findById(accountId)
            ?: throw BusinessException(ErrorCode.ACCOUNT_NOT_FOUND)

        val contributorGroup = contributorService.findGroupById(ContributorGroupId.from(request.contributorGroupId))
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.isParticipating(accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        val novel = novelService.findNovel(contributorGroup.novelId)
            ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        val chapterTexts = novel.findChapterTexts(ChapterId.from(request.chapterId))

        val authors = accountService.findByIds(chapterTexts.map { it.accountId }).associateBy { it.idValue }

        return Response(
            items = chapterTexts.map { chapterText ->
                Response.ChapterText(
                    id = chapterText.idValue.toString(),
                    content = chapterText.content,
                    authorName = authors[chapterText.accountId]?.name ?: Account.UNKNOWN,
                    createdAt = chapterText.createdAt
                )
            }
        )
    }

    data class Request(
        val accountId: String,
        val contributorGroupId: String,
        val chapterId: String
    )

    data class Response(
        val items: List<ChapterText>
    ) {
        data class ChapterText(
            val id: String,
            val content: String,
            val authorName: String,
            val createdAt: LocalDateTime
        )
    }
}
