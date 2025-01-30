package lab.ujumeonji.literaturebackend.usecase.post

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.post.ContributorGroupRecruitmentService
import lab.ujumeonji.literaturebackend.domain.post.command.CreateNovelRoomRecruitmentPostCommand
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CreateNovelRoomRecruitmentPostUseCase @Autowired constructor(
    private val contributorService: ContributorService,
    private val contributorGroupRecruitmentService: ContributorGroupRecruitmentService,
) : UseCase<CreateNovelRoomRecruitmentPostUseCase.Request, CreateNovelRoomRecruitmentPostUseCase.Response> {
    override fun execute(
        request: Request,
        executedAt: LocalDateTime
    ): Response {
        val accountId = AccountId.from(request.accountId)
        val contributorGroupId = ContributorGroupId.from(request.novelRoomId)
        val contributorGroup = contributorService.findGroupById(contributorGroupId)
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.hasManagePermission(accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        val recruitment = contributorGroupRecruitmentService.createNovelRecruitmentPost(
            command = CreateNovelRoomRecruitmentPostCommand(
                title = request.title,
                content = request.content,
                link = request.link,
                authorId = accountId,
                contributorGroupId = contributorGroupId
            ),
            now = executedAt
        )

        return Response(
            id = recruitment.id.toString(),
        )
    }

    data class Request(
        val accountId: String,
        val novelRoomId: String,
        val title: String,
        val content: String,
        val link: String,
    )

    data class Response(
        val id: String,
    )
}
