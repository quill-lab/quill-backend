package lab.ujumeonji.literaturebackend.usecase.novel

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
@Transactional
class UpdateDraftChapterTextUseCase(
    private val novelService: NovelService,
    private val accountService: AccountService,
    private val contributorService: ContributorService,
) : UseCase<UpdateDraftChapterTextUseCase.Request, UpdateDraftChapterTextUseCase.Response> {
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val accountId = AccountId.from(request.accountId)
        accountService.findById(accountId)
            ?: throw BusinessException(ErrorCode.ACCOUNT_NOT_FOUND)

        val contributorGroup =
            contributorService.findGroupById(ContributorGroupId.from(request.contributorGroupId))
                ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.isParticipating(accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        val novel =
            novelService.findNovel(contributorGroup.novelId)
                ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        val contributorInfo = contributorGroup.findContributorInfoByAccountId(accountId)
            .orElseThrow { BusinessException(ErrorCode.CONTRIBUTOR_NOT_FOUND) }

        val updated = novel.updateDraftChapterText(
            ChapterId.from(request.chapterId),
            contributorInfo,
            request.content,
            executedAt,
        )

        if (!updated) {
            throw BusinessException(ErrorCode.BAD_REQUEST)
        }

        return Response(
            success = true,
        )
    }

    data class Request(
        val accountId: String,
        val contributorGroupId: String,
        val chapterId: String,
        val content: String,
    )

    data class Response(
        val success: Boolean,
    )
}
