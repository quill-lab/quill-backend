package lab.ujumeonji.literaturebackend.usecase.contributor

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRequestId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class ApproveJoinRequestUseCase(
    private val contributorService: ContributorService,
) : UseCase<ApproveJoinRequestUseCase.Request, ApproveJoinRequestUseCase.Response> {
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val contributorGroup =
            contributorService.findGroupById(ContributorGroupId.from(request.novelRoomId))
                ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        val novelRoomManagerAccountId = AccountId.from(request.adminAccountId)
        val contributorRequestId = ContributorRequestId.from(request.contributorRequestId)

        contributorGroup.approveJoinRequest(novelRoomManagerAccountId, contributorRequestId, executedAt)

        return Response()
    }

    data class Request(
        val adminAccountId: String,
        val novelRoomId: String,
        val contributorRequestId: String,
    )

    data class Response(
        val success: Boolean = true,
    )
}
