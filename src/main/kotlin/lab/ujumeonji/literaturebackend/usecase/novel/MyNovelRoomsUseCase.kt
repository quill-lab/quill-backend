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
class MyNovelRoomsUseCase(
    private val accountService: AccountService,
    private val contributorService: ContributorService,
    private val novelService: NovelService,
) : UseCase<MyNovelRoomsUseCase.Request, MyNovelRoomsUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        return Response(emptyList(), 1, 1)
    }

    data class Request(
        val accountId: Long,
    )

    data class Response(
        val result: List<ResponseItem>,
        val totalCount: Int,
        val page: Int,
    ) {

        data class ResponseItem(
            val id: Long,
            val category: Category,
            val title: String,
            val createdAt: LocalDateTime,
            val completedAt: LocalDateTime,
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
