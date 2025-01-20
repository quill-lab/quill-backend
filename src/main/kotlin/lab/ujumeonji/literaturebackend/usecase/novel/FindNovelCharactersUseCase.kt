package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class FindNovelCharactersUseCase(
    private val novelService: NovelService,
    private val accountService: AccountService,
) : UseCase<FindNovelCharactersUseCase.Request, List<FindNovelCharactersUseCase.Response>> {

    override fun execute(request: Request, executedAt: LocalDateTime): List<Response> {
        val novel = novelService.findNovel(request.novelId)
            ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        val accountIds = novel.characters.mapNotNull { it.lastUpdatedBy }
        val accountMap = accountService.findByIds(accountIds).associateBy { it.id }

        return novel.characters.map { character ->
            Response(
                id = character.id,
                name = character.name,
                description = character.description,
                profileImage = character.profileImage,
                updatedAt = character.updatedAt,
                updatedBy = character.lastUpdatedBy?.let { accountId ->
                    accountMap[accountId]?.let { account ->
                        Response.LastCharacterUpdatedBy(
                            id = account.id,
                            name = account.name,
                        )
                    }
                },
            )
        }
    }

    data class Request(
        val novelId: Long,
    )

    data class Response(
        val id: Long,
        val name: String,
        val description: String,
        val profileImage: String,
        val updatedAt: LocalDateTime?,
        val updatedBy: LastCharacterUpdatedBy?,
    ) {

        data class LastCharacterUpdatedBy(
            val id: Long,
            val name: String,
        )
    }
}
