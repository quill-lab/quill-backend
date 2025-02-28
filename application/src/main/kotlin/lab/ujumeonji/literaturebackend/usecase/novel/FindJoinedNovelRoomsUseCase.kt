package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroup
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRole
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.contributor.command.FindContributorGroupsCommand
import lab.ujumeonji.literaturebackend.domain.contributor.command.NovelRoomSortTypeEnum
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.data.domain.Page
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class FindJoinedNovelRoomsUseCase(
    private val accountService: AccountService,
    private val contributorService: ContributorService,
    private val novelService: NovelService,
) : UseCase<FindJoinedNovelRoomsUseCase.Request, FindJoinedNovelRoomsUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val accountId = AccountId.from(request.accountId)
        val command = FindContributorGroupsCommand(
            page = request.page,
            size = request.size,
            sort = request.sort
        )

        val page: Page<ContributorGroup> = contributorService.findContributorGroups(accountId, command)
        val contributorGroups = page.content

        val me = accountService.findById(accountId)
            ?: throw BusinessException(ErrorCode.ACCOUNT_NOT_FOUND)

        val novels = novelService.findNovels(contributorGroups.map { it.novelId })
            .associateBy { it.id }

        val accounts = accountService.findByIds(contributorGroups.mapNotNull { it.activeContributorAccountId })
            .associateBy { it.id }

        val result = contributorGroups.mapNotNull { contributorGroup ->
            novels[contributorGroup.novelId]?.let { novel ->
                with(contributorGroup) {
                    Response.ResponseItem(
                        id = id.toString(),
                        category = Response.ResponseItem.Category(
                            name = novel.category.name,
                            alias = novel.category.alias
                        ),
                        title = novel.title,
                        createdAt = createdAt,
                        completedAt = completedAt,
                        role = (getCollaboratorRole(me.id) ?: ContributorRole.COLLABORATOR).name,
                        contributorCount = contributorCount,
                        maxContributorCount = maxContributorCount,
                        currentAuthor = activeContributorAccountId?.let { authorId ->
                            accounts[authorId]?.let { author ->
                                Response.ResponseItem.Author(
                                    id = author.id.toString(),
                                    name = author.name
                                )
                            }
                        },
                        status = status.name
                    )
                }
            }
        }

        return Response(
            result = result,
            totalCount = page.totalElements,
            size = request.size,
            page = request.page
        )
    }

    data class Request(
        val accountId: String,
        val page: Int,
        val size: Int,
        val sort: NovelRoomSortTypeEnum = NovelRoomSortTypeEnum.LATEST,
    )

    data class Response(
        val result: List<ResponseItem>,
        val totalCount: Long,
        val size: Int,
        val page: Int,
    ) {
        data class ResponseItem(
            val id: String,
            val category: Category,
            val title: String,
            val createdAt: LocalDateTime,
            val completedAt: LocalDateTime?,
            val role: String,
            val contributorCount: Int,
            val maxContributorCount: Int,
            val currentAuthor: Author?,
            val status: String,
        ) {
            data class Category(
                val name: String,
                val alias: String,
            )

            data class Author(
                val id: String,
                val name: String,
            )
        }
    }
}
