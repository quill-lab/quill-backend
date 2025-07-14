package lab.ujumeonji.literaturebackend.usecase.post

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.post.ContributorGroupRecruitmentId
import lab.ujumeonji.literaturebackend.domain.post.ContributorGroupRecruitmentService
import lab.ujumeonji.literaturebackend.domain.post.command.UpdateContributorGroupRecruitmentCommand
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class UpdateNovelRoomRecruitmentPostUseCase
@Autowired
constructor(
    private val accountService: AccountService,
    private val contributorService: ContributorService,
    private val contributorGroupRecruitmentService: ContributorGroupRecruitmentService,
) : UseCase<UpdateNovelRoomRecruitmentPostUseCase.Request, UpdateNovelRoomRecruitmentPostUseCase.Response> {
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val accountId = AccountId.from(request.accountId)
        accountService.findById(accountId)
            ?: throw BusinessException(ErrorCode.ACCOUNT_NOT_FOUND)

        val contributorGroupId = ContributorGroupId.from(request.novelRoomId)
        val contributorGroup =
            contributorService.findGroupById(contributorGroupId)
                ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.hasManagePermission(accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        val recruitmentId = ContributorGroupRecruitmentId.from(request.recruitmentId)
        val recruitment = contributorGroupRecruitmentService.findRecruitmentById(recruitmentId)
            ?: throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)

        recruitment.update(
            UpdateContributorGroupRecruitmentCommand(
                title = request.title,
                content = request.content,
                link = request.link,
            ),
            executedAt,
        )

        return Response(
            id = recruitment.idValue.toString(),
        )
    }

    data class Request(
        val accountId: String,
        val novelRoomId: String,
        val title: String,
        val content: String,
        val link: String,
        val recruitmentId: String,
    )

    data class Response(
        val id: String,
    )
}
