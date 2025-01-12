package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.service.domain.account.AccountService
import lab.ujumeonji.literaturebackend.service.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.service.domain.contributor.command.CreateContributorGroupCommand
import lab.ujumeonji.literaturebackend.service.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.service.domain.novel.command.CreateNovelCommand
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class CreateNovelRoomUseCase(
    private val accountService: AccountService,
    private val contributorService: ContributorService,
    private val novelService: NovelService,
) : UseCase<CreateNovelRoomUseCase.Request, CreateNovelRoomUseCase.Response> {

    override fun execute(request: Request): Response {
        val novel = novelService.createNovel(
            command = CreateNovelCommand(
                name = request.name,
                description = request.description,
                coverImage = request.novelCoverImage,
            ),
            now = request.executedAt
        )

        val contributorGroup = contributorService.createContributorGroup(
            command = CreateContributorGroupCommand(
                novelId = novel.id,
                name = request.name,
                description = request.description,
                maxContributorCount = request.maxContributorCount,
            ),
            now = request.executedAt
        )

        contributorGroup.addHostContributor(request.creatorId, request.executedAt)

        return Response(contributorGroup.id)
    }

    data class Request(
        val creatorId: Long,
        val name: String,
        val description: String,
        val maxContributorCount: Int,
        val novelCoverImage: String,
        val executedAt: LocalDateTime,
    )

    data class Response(
        val novelRoomId: Long
    )
}
