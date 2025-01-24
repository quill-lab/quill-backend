package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupStatus
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRole
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
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

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val contributorGroup = contributorService.findById(request.contributorGroupId)
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        val me = accountService.findById(request.accountId)
            ?: throw BusinessException(ErrorCode.ACCOUNT_NOT_FOUND)

        val novel = novelService.findById(contributorGroup.novelId)
            ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        return Response(
            id = contributorGroup.id,
            category = Response.Category(
                name = novel.category.name,
                alias = novel.category.alias
            ),
            title = novel.title,
            description = novel.description,
            synopsis = novel.synopsis,
            createdAt = contributorGroup.createdAt,
            completedAt = contributorGroup.completedAt,
            role = contributorGroup.getCollaboratorRole(me.id),
            contributorCount = contributorGroup.contributorCount,
            maxContributorCount = contributorGroup.maxContributorCount,
            author = Response.Author(
                id = me.id,
                name = me.name
            ),
            status = contributorGroup.status,
            tags = novel.hashtaggedTags
        )
    }

    data class Request(
        val accountId: Long,
        val contributorGroupId: Long,
    )

    data class Response(
        val id: Long,
        val category: Category,
        val title: String,
        val description: String,
        val synopsis: String?,
        val createdAt: LocalDateTime,
        val completedAt: LocalDateTime?,
        val role: ContributorRole,
        val contributorCount: Int,
        val maxContributorCount: Int,
        val author: Author,
        val status: ContributorGroupStatus,
        val tags: List<String>
    ) {

        data class Category(
            val name: String,
            val alias: String,
        )

        data class Author(
            val id: Long,
            val name: String,
        )
    }
}
