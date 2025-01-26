package lab.ujumeonji.literaturebackend.usecase.novel

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
class UpdateParticipantOrderUseCase(
    private val contributorService: ContributorService,
) : UseCase<UpdateParticipantOrderUseCase.Request, UpdateParticipantOrderUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val accountId = AccountId.from(request.accountId)
        val contributorId = ContributorId.from(request.contributorId)
        val novelRoomId = ContributorGroupId.from(request.novelRoomId)

        val contributorGroup = contributorService.findGroupById(novelRoomId)
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.hasManagePermission(accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        if (!contributorGroup.isParticipating(accountId)) {
            throw BusinessException(ErrorCode.CONTRIBUTOR_NOT_FOUND)
        }

        contributorGroup.updateWritingOrder(contributorId, request.writingOrder)

        return Response(
            id = request.contributorId,
        )
    }

    data class Request(
        val accountId: String,
        val novelRoomId: String,
        val contributorId: String,
        val writingOrder: Int,
    )

    data class Response(
        val id: String,
    )
}
