package lab.ujumeonji.literaturebackend.usecase.contributor

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupRepository
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class ApproveJoinRequestUseCase(
    private val contributorGroupRepository: ContributorGroupRepository,
) : UseCase<ApproveJoinRequestUseCase.Request, ApproveJoinRequestUseCase.Response> {
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val contributorGroup = contributorGroupRepository.findById(ContributorGroupId.from(request.novelRoomId).id)
            .orElseThrow { IllegalArgumentException("Novel room not found: ${request.novelRoomId}") }

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
