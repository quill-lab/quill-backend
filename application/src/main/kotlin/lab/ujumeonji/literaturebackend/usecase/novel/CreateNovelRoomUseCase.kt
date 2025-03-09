package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.contributor.command.CreateContributorGroupCommand
import lab.ujumeonji.literaturebackend.domain.novel.NovelId
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.domain.novel.command.CreateNovelCommand
import lab.ujumeonji.literaturebackend.domain.novel.command.NovelCategoryEnum
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
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
        val accountId = AccountId.from(request.creatorId)
        // TODO: Temporarily commented out to allow test data creation
        // This check prevents users from creating multiple contributor groups
        // Uncomment this when the test data creation is complete
        // if (contributorService.hasOwnContributorGroup(accountId)) {
        //     throw BusinessException(ErrorCode.DUPLICATE_CONTRIBUTOR_GROUP)
        // }

        val novel = createNovel(request, executedAt)

        val contributorGroup = createContributorGroup(request, novel.idValue, executedAt)

        return Response("${contributorGroup.idValue}")
    }

    private fun createNovel(request: Request, executedAt: LocalDateTime) = novelService.createNovel(
        command = CreateNovelCommand(
            title = request.title,
            description = request.description,
            category = request.category.toNovelCategory(),
            coverImage = request.novelCoverImage,
            synopsis = request.synopsis,
            tags = request.tags,
        ),
        now = executedAt
    )

    private fun createContributorGroup(request: Request, novelId: NovelId, executedAt: LocalDateTime) =
        contributorService.createContributorGroup(
            command = CreateContributorGroupCommand(
                novelId = novelId,
                ownerId = AccountId.from(request.creatorId),
                tags = request.tags,
                maxContributorCount = request.maxContributorCount,
            ),
            now = executedAt
        )

    data class Request(
        val creatorId: String,
        val title: String,
        val description: String,
        val category: NovelCategoryEnum,
        val tags: List<String>,
        val synopsis: String?,
        val maxContributorCount: Int,
        val novelCoverImage: String?,
    )

    data class Response(
        val novelRoomId: String
    )
}
