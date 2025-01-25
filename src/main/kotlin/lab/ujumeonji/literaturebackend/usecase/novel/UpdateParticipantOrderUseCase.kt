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
        contributorService.findGroupById(request.novelRoomId)
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorService.hasManagePermission(request.novelRoomId, request.accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        if (!contributorService.isParticipating(request.novelRoomId, request.participantId)) {
            throw BusinessException(ErrorCode.CONTRIBUTOR_NOT_FOUND)
        }

        if (!contributorService.hasManagePermission(request.novelRoomId, request.accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        contributorService.updateParticipantOrder(
            contributorGroupId = request.novelRoomId,
            contributorId = request.participantId,
            writingOrder = request.writingOrder,
            now = executedAt,
        )

        return Response(
            id = request.participantId,
        )
    }

    data class Request(
        val accountId: Long,
        val novelRoomId: Long,
        val participantId: Long,
        val writingOrder: Int,
    )

    data class Response(
        val id: Long,
    )
}
