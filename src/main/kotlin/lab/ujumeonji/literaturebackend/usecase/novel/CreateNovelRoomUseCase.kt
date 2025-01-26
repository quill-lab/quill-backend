package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.contributor.command.CreateContributorGroupCommand
import lab.ujumeonji.literaturebackend.domain.novel.NovelCategory
import lab.ujumeonji.literaturebackend.domain.novel.NovelId
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.domain.novel.command.CreateNovelCommand
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
        if (contributorService.hasOwnContributorGroup(accountId)) {
            throw BusinessException(ErrorCode.DUPLICATE_CONTRIBUTOR_GROUP)
        }

        val novel = createNovel(request, executedAt)

        val contributorGroup = createContributorGroup(request, novel.id, executedAt)

        return Response("${contributorGroup.id}")
    }

    private fun createNovel(request: Request, executedAt: LocalDateTime) = novelService.createNovel(
        command = CreateNovelCommand(
            title = request.title,
            description = request.description,
            category = request.category,
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
        val category: NovelCategory,
        val tags: List<String>,
        val synopsis: String?,
        val maxContributorCount: Int,
        val novelCoverImage: String?,
    )

    data class Response(
        val novelRoomId: String
    )
}
