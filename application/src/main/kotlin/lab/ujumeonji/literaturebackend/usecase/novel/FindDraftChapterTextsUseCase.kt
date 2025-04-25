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
class FindDraftChapterTextsUseCase(
    private val accountService: AccountService,
    private val contributorService: ContributorService,
    private val novelService: NovelService,
) : UseCase<FindDraftChapterTextsUseCase.Request, FindDraftChapterTextsUseCase.Response> {
    @Transactional(readOnly = true)
    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val accountId = AccountId.from(request.accountId)
        val contributorGroupId = ContributorGroupId.from(request.contributorGroupId)
        val chapterId = ChapterId.from(request.chapterId)

        accountService.findById(accountId)
            ?: throw BusinessException(ErrorCode.ACCOUNT_NOT_FOUND)

        val contributorGroup = contributorService.findGroupById(contributorGroupId)
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.isParticipating(accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_VIEW)
        }

        val contributor = contributorGroup.findContributorInfoByAccountId(accountId)
            .orElseThrow { BusinessException(ErrorCode.NO_PERMISSION_TO_VIEW) }

        val novel = novelService.findNovel(contributorGroup.novelId)
            ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        val draftText = novel.findDraftChapterText(chapterId)
            .orElseThrow { BusinessException(ErrorCode.DRAFT_CHAPTER_TEXT_NOT_FOUND) }

        val account = accountService.findById(contributor.accountId)

        return Response(
            id = draftText.idValue.toString(),
            content = draftText.content,
            accountId = account?.idValue.toString(),
            authorName = account?.name ?: Account.UNKNOWN,
            createdAt = draftText.createdAt,
            updatedAt = draftText.updatedAt,
        )
    }

    data class Request(
        val accountId: String,
        val contributorGroupId: String,
        val chapterId: String,
    )

    data class Response(
        val id: String,
        val content: String,
        val accountId: String,
        val authorName: String,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime,
    )
}
