package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.contributor.command.CreateContributorGroupCommand
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.domain.novel.command.CreateNovelCommand
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

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val novel = createNovel(request, executedAt)
        val contributorGroup = createContributorGroup(request, novel.id, executedAt)

        return Response(contributorGroup.id)
    }

    private fun createNovel(request: Request, executedAt: LocalDateTime) = novelService.createNovel(
        command = CreateNovelCommand(
            name = request.title,
            description = request.description,
            coverImage = request.novelCoverImage,
            tags = request.tags,
        ),
        now = executedAt
    )

    private fun createContributorGroup(request: Request, novelId: Long, executedAt: LocalDateTime) =
        contributorService.createContributorGroup(
            command = CreateContributorGroupCommand(
                novelId = novelId,
                ownerId = request.creatorId,
                name = request.title,
                description = request.description,
                tags = request.tags,
                maxContributorCount = request.maxContributorCount,
            ),
            now = executedAt
        )

    data class Request(
        val creatorId: Long,
        val title: String,
        val description: String,
        val tags: List<String>,
        val maxContributorCount: Int,
        val novelCoverImage: String?,
    )

    data class Response(
        val novelRoomId: Long
    )
}
