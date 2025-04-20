package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
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
class UpdateChapterUseCase(
    private val contributorService: ContributorService,
    private val novelService: NovelService,
) : UseCase<UpdateChapterUseCase.Request, UpdateChapterUseCase.Response> {
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val contributorGroup =
            contributorService.findGroupById(ContributorGroupId.from(request.contributorGroupId))
                ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.hasManagePermission(AccountId.from(request.accountId))) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        val novel =
            novelService.findNovel(contributorGroup.novelId)
                ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        val updated =
            novel.updateChapter(
                ChapterId.from(request.chapterId),
                request.title,
                executedAt,
            )

        if (!updated) {
            throw BusinessException(ErrorCode.CHAPTER_NOT_FOUND)
        }

        return Response(id = request.chapterId)
    }

    data class Request(
        val accountId: String,
        val contributorGroupId: String,
        val chapterId: String,
        val title: String?,
    )

    data class Response(
        val id: String,
    )
}
