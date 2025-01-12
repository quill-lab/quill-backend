package lab.ujumeonji.literaturebackend.usecase.novel

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
    private val contributorService: ContributorService,
    private val novelService: NovelService,
) : UseCase<CreateNovelRoomUseCase.Request, CreateNovelRoomUseCase.Response> {

    override fun execute(request: Request): Response {
        val novel = createNovel(request)
        val contributorGroup = createContributorGroup(request, novel.id)

        contributorGroup.addHostContributor(request.creatorId, request.executedAt)

        return Response(contributorGroup.id)
    }

    private fun createNovel(request: Request) = novelService.createNovel(
        command = CreateNovelCommand(
            name = request.title,
            description = request.description,
            coverImage = request.novelCoverImage,
        ),
        now = request.executedAt
    )

    private fun createContributorGroup(request: Request, novelId: Long) = contributorService.createContributorGroup(
        command = CreateContributorGroupCommand(
            novelId = novelId,
            name = request.title,
            description = request.description,
            maxContributorCount = request.maxContributorCount,
        ),
        now = request.executedAt
    )

    data class Request(
        val creatorId: Long,
        val title: String,
        val description: String,
        val maxContributorCount: Int,
        val novelCoverImage: String?,
        val executedAt: LocalDateTime,
    )

    data class Response(
        val novelRoomId: Long
    )
}
