package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.service.domain.account.AccountService
import lab.ujumeonji.literaturebackend.service.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.service.domain.contributor.command.CreateContributorGroupCommand
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class CreateNovelRoomUseCase(
    private val accountService: AccountService,
    private val contributorService: ContributorService,
) : UseCase<CreateNovelRoomUseCase.Request, CreateNovelRoomUseCase.Response> {

    override fun execute(request: Request): Response {
        val contributorGroup = contributorService.createContributorGroup(
            CreateContributorGroupCommand(
                name = request.name,
                description = request.description,
                maxContributorCount = request.maxContributorCount,
            )
        )

        contributorGroup.addHostContributor(request.creatorId, request.executedAt)

        return Response(contributorGroup.id)
    }

    data class Request(
        val creatorId: Long,
        val name: String,
        val description: String,
        val maxContributorCount: Int,
        val executedAt: LocalDateTime,
    )

    data class Response(
        val novelRoomId: Long
    )
}
