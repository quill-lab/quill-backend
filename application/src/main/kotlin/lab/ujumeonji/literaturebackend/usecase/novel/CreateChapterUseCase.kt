package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class CreateChapterUseCase(
    private val contributorService: ContributorService,
    private val novelService: NovelService,
) : UseCase<CreateChapterUseCase.Request, CreateChapterUseCase.Response> {
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

        if (!contributorGroup.hasContributors()) {
            throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_EMPTY)
        }

        val novel =
            novelService.findNovel(contributorGroup.novelId)
                ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        val createdChapter =
            novel.createChapter(contributorGroup.contributors, request.title, request.description, executedAt)

        return Response(createdChapter.idValue.toString())
    }

    data class Request(
        val accountId: String,
        val contributorGroupId: String,
        val title: String,
        val description: String? = null,
    )

    data class Response(
        val id: String,
    )
}
