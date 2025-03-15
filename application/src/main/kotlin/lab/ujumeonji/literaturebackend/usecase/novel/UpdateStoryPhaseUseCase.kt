package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.domain.novel.command.StoryPhaseEnum
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class UpdateStoryPhaseUseCase(
    private val accountService: AccountService,
    private val novelService: NovelService,
    private val contributorService: ContributorService,
) : UseCase<UpdateStoryPhaseUseCase.Request, UpdateStoryPhaseUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val accountId = AccountId.from(request.accountId)
        accountService.findById(accountId)
            ?: throw BusinessException(ErrorCode.ACCOUNT_NOT_FOUND)

        val contributorGroup = contributorService.findGroupById(ContributorGroupId.from(request.contributorGroupId))
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.hasManagePermission(accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        val novel = novelService.findNovel(contributorGroup.novelId)
            ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        novel.updatePhase(
            request.phase.toStoryPhase(),
            request.startChapterNumber,
            request.endChapterNumber,
            request.description,
            executedAt
        )

        return Response(
            id = novel.idValue.toString()
        )
    }

    data class Request(
        val accountId: String,
        val contributorGroupId: String,
        val phase: StoryPhaseEnum,
        val startChapterNumber: Int?,
        val endChapterNumber: Int?,
        val description: String?,
    )

    data class Response(
        val id: String,
    )
}
