package lab.ujumeonji.literaturebackend.usecase.novel

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
        val contributorGroup = contributorService.findGroupById(request.novelRoomId)
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.hasManagePermission(request.accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        if (!contributorGroup.isParticipating(request.contributorId)) {
            throw BusinessException(ErrorCode.CONTRIBUTOR_NOT_FOUND)
        }

        contributorGroup.updateWritingOrder(request.contributorId, request.writingOrder)

        return Response(
            id = request.contributorId,
        )
    }

    data class Request(
        val accountId: Long,
        val novelRoomId: Long,
        val contributorId: Long,
        val writingOrder: Int,
    )

    data class Response(
        val id: Long,
    )
}
