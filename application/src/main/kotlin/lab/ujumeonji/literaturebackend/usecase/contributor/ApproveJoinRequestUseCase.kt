package lab.ujumeonji.literaturebackend.usecase.contributor

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
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
        val contributorGroup = contributorService.findGroupById(ContributorGroupId.from(request.novelRoomId))
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        val adminAccountId = AccountId.from(request.adminAccountId)
        val requesterAccountId = AccountId.from(request.requesterAccountId)

        contributorGroup.approveJoinRequest(adminAccountId, requesterAccountId, executedAt)

        return Response()
    }

    data class Request(
        val adminAccountId: String,
        val novelRoomId: String,
        val requesterAccountId: String,
    )

    data class Response(
        val success: Boolean = true,
    )
}
