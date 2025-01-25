package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.domain.novel.command.AddCharacterCommand
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class AddNovelCharacterUseCase(
    private val novelService: NovelService,
    private val contributorService: ContributorService,
) : UseCase<AddNovelCharacterUseCase.Request, AddNovelCharacterUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        if (!contributorService.hasManagePermission(request.contributorGroupId, request.accountId)) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        val contributorGroup = contributorService.findGroupById(request.contributorGroupId)
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        val novel = novelService.findNovel(contributorGroup.novelId)
            ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        val addedCharacterId = novelService.addCharacter(
            novelId = novel.id,
            command = AddCharacterCommand(
                name = request.name,
                description = request.description,
            ),
            now = executedAt
        )

        return Response(
            id = addedCharacterId,
        )
    }

    data class Request(
        val accountId: Long,
        val contributorGroupId: Long,
        val name: String,
        val description: String,
    )

    data class Response(
        val id: Long,
    )
}
