package lab.ujumeonji.literaturebackend.usecase.contributor

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class RemoveContributorUseCase(
    private val contributorService: ContributorService,
) : UseCase<RemoveContributorUseCase.Request, RemoveContributorUseCase.Response> {
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val contributorGroup =
            contributorService.findGroupById(ContributorGroupId.from(request.novelRoomId))
                ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        val contributorGroupManagerId = AccountId.from(request.adminAccountId)
        val targetContributorId = ContributorId.from(request.targetContributorId)
        val success = contributorGroup.removeContributor(contributorGroupManagerId, targetContributorId, executedAt)

        return Response(success)
    }

    data class Request(
        val adminAccountId: String,
        val novelRoomId: String,
        val targetContributorId: String,
    )

    data class Response(
        val success: Boolean,
    )
}
