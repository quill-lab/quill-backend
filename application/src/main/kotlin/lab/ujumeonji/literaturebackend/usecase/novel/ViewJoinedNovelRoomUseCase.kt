package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRole
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.contributor.command.ContributorGroupStatusEnum
import lab.ujumeonji.literaturebackend.domain.contributor.command.ContributorRoleEnum
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class ViewJoinedNovelRoomUseCase(
    private val accountService: AccountService,
    private val contributorService: ContributorService,
    private val novelService: NovelService,
) : UseCase<ViewJoinedNovelRoomUseCase.Request, ViewJoinedNovelRoomUseCase.Response> {
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val accountId = AccountId.from(request.accountId)
        val contributorGroupId = ContributorGroupId.from(request.contributorGroupId)

        val contributorGroup =
            contributorService.findGroupById(contributorGroupId)
                ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        val me =
            accountService.findById(accountId)
                ?: throw BusinessException(ErrorCode.ACCOUNT_NOT_FOUND)

        val novel =
            novelService.findNovel(contributorGroup.novelId)
                ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        return Response(
            id = contributorGroup.idValue.toString(),
            category =
                Response.Category(
                    name = novel.category.name,
                    alias = novel.category.alias,
                ),
            title = novel.title,
            synopsis = novel.synopsis,
            createdAt = contributorGroup.createdAt,
            completedAt = contributorGroup.completedAt,
            role =
                ContributorRoleEnum.fromContributorRole(
                    contributorGroup.getCollaboratorRole(me.idValue) ?: ContributorRole.COLLABORATOR,
                ),
            contributorCount = contributorGroup.contributorCount,
            maxContributorCount = contributorGroup.maxContributorCount,
            status = ContributorGroupStatusEnum.fromContributorGroupStatus(contributorGroup.status),
            tags = novel.tagNames,
        )
    }

    data class Request(
        val accountId: String,
        val contributorGroupId: String,
    )

    data class Response(
        val id: String,
        val category: Category,
        val title: String,
        val synopsis: String?,
        val createdAt: LocalDateTime,
        val completedAt: LocalDateTime?,
        val role: ContributorRoleEnum,
        val contributorCount: Int,
        val maxContributorCount: Int,
        val status: ContributorGroupStatusEnum,
        val tags: List<String>,
    ) {
        data class Category(
            val name: String,
            val alias: String,
        )
    }
}
