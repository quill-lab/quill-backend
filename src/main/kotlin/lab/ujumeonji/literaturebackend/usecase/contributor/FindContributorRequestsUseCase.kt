package lab.ujumeonji.literaturebackend.usecase.contributor

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRequestStatus
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.contributor.command.FindContributorRequestHistoriesCommand
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class FindContributorRequestsUseCase(
    private val contributorService: ContributorService,
) : UseCase<FindContributorRequestsUseCase.Request, FindContributorRequestsUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val command = FindContributorRequestHistoriesCommand(
            page = request.page,
            size = request.size
        )

        val page = contributorService.findContributorRequestHistories(AccountId.from(request.accountId), command)

        return Response(
            result = page.content.map { history ->
                Response.ResponseItem(
                    id = history.id.toString(),
                    title = history.title,
                    status = history.status,
                    requestedAt = history.requestedAt,
                    joinedAt = history.joinedAt,
                    leftAt = history.leftAt
                )
            },
            totalCount = page.totalElements,
            size = request.size,
            page = request.page
        )
    }

    data class Request(
        val accountId: String,
        val page: Int,
        val size: Int,
    )

    data class Response(
        val result: List<ResponseItem>,
        val totalCount: Long,
        val size: Int,
        val page: Int,
    ) {
        data class ResponseItem(
            val id: String,
            val title: String,
            val status: ContributorRequestStatus,
            val requestedAt: LocalDateTime,
            val joinedAt: LocalDateTime?,
            val leftAt: LocalDateTime?,
        )
    }
}
