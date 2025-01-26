package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRole
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class FindNovelRoomParticipantsUseCase(
    private val accountService: AccountService,
    private val contributorService: ContributorService,
) : UseCase<FindNovelRoomParticipantsUseCase.Request, List<FindNovelRoomParticipantsUseCase.Response>> {

    override fun execute(request: Request, executedAt: LocalDateTime): List<Response> {
        val contributorGroup = contributorService.findGroupById(request.novelRoomId)
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.isParticipating(request.accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_VIEW)
        }

        val contributors = contributorGroup.contributors
            .sortedBy { it.writingOrder ?: Int.MAX_VALUE }
        val accountIds = contributors.map { it.accountId }
        val accounts = accountService.findByIds(accountIds).associateBy { it.id }

        return contributors.mapNotNull { contributor ->
            accounts[contributor.accountId]?.let { account ->
                Response(
                    id = account.id,
                    nickname = account.name,
                    role = contributor.role,
                    writingOrder = contributor.writingOrder,
                    joinedAt = contributor.createdAt,
                )
            }
        }
    }

    data class Request(
        val accountId: Long,
        val novelRoomId: Long,
    )

    data class Response(
        val id: Long,
        val nickname: String,
        val role: ContributorRole,
        val writingOrder: Int?,
        val joinedAt: LocalDateTime,
    )
}
