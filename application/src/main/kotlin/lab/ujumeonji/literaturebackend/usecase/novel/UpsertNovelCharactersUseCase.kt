package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.domain.novel.command.UpsertCharactersCommand
import lab.ujumeonji.literaturebackend.domain.novel.command.UpsertNovelCharacterCommand
import lab.ujumeonji.literaturebackend.domain.novel.dto.NovelCharacterData
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class UpsertNovelCharactersUseCase(
    private val novelService: NovelService,
    private val contributorService: ContributorService,
) : UseCase<UpsertNovelCharactersUseCase.Request, UpsertNovelCharactersUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val contributorGroup = contributorService.findGroupById(ContributorGroupId.from(request.contributorGroupId))
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.hasManagePermission(AccountId.from(request.accountId))) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        val novel = novelService.findNovel(contributorGroup.novelId)
            ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        val characters = novel.replaceCharacters(
            request.characters.map { command ->
                UpsertCharactersCommand(
                    command.name,
                    command.description
                )
            },
            AccountId.from(request.accountId),
            executedAt
        )

        return Response(
            characters = characters.map { character ->
                NovelCharacterData(
                    id = character.id.toString(),
                )
            }
        )
    }

    data class Request(
        val accountId: String,
        val contributorGroupId: String,
        val characters: List<UpsertNovelCharacterCommand>
    )

    data class Response(
        val characters: List<NovelCharacterData>
    )
}
