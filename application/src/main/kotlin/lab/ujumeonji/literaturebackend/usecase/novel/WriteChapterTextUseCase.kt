package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.novel.ChapterId
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode // Ensure ErrorCode is imported
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class WriteChapterTextUseCase(
    private val novelService: NovelService,
    private val accountService: AccountService,
    private val contributorService: ContributorService,
) : UseCase<WriteChapterTextUseCase.Request, WriteChapterTextUseCase.Response> {

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

        val contributor = contributorGroup.findContributorByAccountId(accountId)

        if (contributor.isEmpty) {
            throw BusinessException(ErrorCode.CONTRIBUTOR_NOT_FOUND)
        }

        val addedChapterText = novel.writeToChapter(
            contributor.get(),
            ChapterId.from(request.chapterId),
            request.content,
            executedAt
        )

        if (addedChapterText.isEmpty()) {
            throw BusinessException(ErrorCode.CHAPTER_NOT_FOUND)
        }

        return Response(
            id = addedChapterText.get().idValue.toString()
        )
    }

    data class Request(
        val accountId: String,
        val contributorGroupId: String,
        val chapterId: String,
        val content: String
    )

    data class Response(
        val id: String
    )
}
