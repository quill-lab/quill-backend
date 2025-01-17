package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupStatus
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRole
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class FindJoinedNovelRoomsUseCase(
    private val accountService: AccountService,
    private val contributorService: ContributorService,
    private val novelService: NovelService,
) : UseCase<FindJoinedNovelRoomsUseCase.Request, FindJoinedNovelRoomsUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val (contributorGroups, totalElements) = contributorService.findByAccountIdWithPaging(
            accountId = request.accountId,
            page = request.page,
            size = request.size,
        )

        val me = accountService.findById(request.accountId) ?: throw IllegalArgumentException("Account not found")

        val novels = novelService.findNovels(contributorGroups.map { it.novelId }).associateBy { it.id }

        val result = contributorGroups.filter {
            novels.containsKey(it.novelId)
        }.map { contributorGroup ->
            val novel = novels[contributorGroup.novelId]!!

            Response.ResponseItem(
                id = contributorGroup.id,
                category = Response.ResponseItem.Category(
                    name = novel.category.name,
                    alias = novel.category.alias
                ),
                title = novel.title,
                createdAt = contributorGroup.createdAt,
                completedAt = contributorGroup.completedAt,
                role = contributorGroup.findRoleByAccountId(me.id),
                contributorCount = contributorGroup.contributorCount,
                maxContributorCount = contributorGroup.maxContributorCount,
                author = Response.ResponseItem.Author(
                    id = me.id,
                    name = me.name
                ),
                status = contributorGroup.status
            )
        }

        return Response(
            result = result,
            totalCount = totalElements.toInt(),
            size = request.size,
            page = request.page,
        )
    }

    data class Request(
        val accountId: Long,
        val page: Int = 0,
        val size: Int = 20,
    )

    data class Response(
        val result: List<ResponseItem>,
        val totalCount: Int,
        val size: Int,
        val page: Int,
    ) {

        data class ResponseItem(
            val id: Long,
            val category: Category,
            val title: String,
            val createdAt: LocalDateTime,
            val completedAt: LocalDateTime?,
            val role: ContributorRole,
            val contributorCount: Int,
            val maxContributorCount: Int,
            val author: Author,
            val status: ContributorGroupStatus,
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
}
