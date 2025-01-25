package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.novel.NovelCategory
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.domain.novel.command.UpdateNovelCommand
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class UpdateNovelUseCase(
    private val accountService: AccountService,
    private val contributorService: ContributorService,
    private val novelService: NovelService,
) : UseCase<UpdateNovelUseCase.Request, UpdateNovelUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        accountService.findById(request.accountId)
            ?: throw BusinessException(ErrorCode.ACCOUNT_NOT_FOUND)

        val contributorGroup = contributorService.findGroupById(request.contributorGroupId)
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorService.hasManagePermission(request.contributorGroupId, request.accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        novelService.update(
            id = contributorGroup.novelId,
            command = UpdateNovelCommand(
                title = request.title,
                description = request.description,
                category = request.category,
                tags = request.tags,
                synopsis = request.synopsis,
            ),
            now = executedAt,
        )

        return Response(contributorGroup.id)
    }

    data class Request(
        val accountId: Long,
        val contributorGroupId: Long,
        val title: String?,
        val description: String?,
        val category: NovelCategory?,
        val tags: List<String>?,
        val synopsis: String?,
    )

    data class Response(
        val id: Long,
    )
}
