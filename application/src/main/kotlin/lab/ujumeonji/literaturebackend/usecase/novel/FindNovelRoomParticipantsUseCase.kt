package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.contributor.command.ContributorRoleEnum
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
        val contributorGroup = contributorService.findGroupById(ContributorGroupId.from(request.novelRoomId))
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.isParticipating(AccountId.from(request.accountId))) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_VIEW)
        }

        val contributors = contributorGroup.contributors
            .sortedBy { it.writingOrder ?: Int.MAX_VALUE }
        val accountIds = contributors.map { it.accountId }
        val accounts = accountService.findByIds(accountIds).associateBy { it.idValue }

        return contributors.mapNotNull { contributor ->
            accounts[contributor.accountId]?.let { account ->
                Response(
                    id = account.idValue.toString(),
                    nickname = account.name,
                    role = ContributorRoleEnum.fromContributorRole(contributor.role),
                    writingOrder = contributor.writingOrder,
                    joinedAt = contributor.createdAt,
                )
            }
        }
    }

    data class Request(
        val accountId: String,
        val novelRoomId: String,
    )

    data class Response(
        val id: String,
        val nickname: String,
        val role: ContributorRoleEnum,
        val writingOrder: Int?,
        val joinedAt: LocalDateTime,
    )
}
