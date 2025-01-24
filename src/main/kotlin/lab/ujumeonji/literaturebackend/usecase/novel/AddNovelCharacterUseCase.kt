package lab.ujumeonji.literaturebackend.usecase.novel

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
) : UseCase<AddNovelCharacterUseCase.Request, AddNovelCharacterUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val novel = novelService.findNovel(request.novelId)
            ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        val character = novelService.addCharacter(
            id = novel.id,
            command = AddCharacterCommand(
                name = request.name,
                description = request.description,
                profileImage = request.profileImage,
                priority = request.priority
            ),
            now = executedAt
        )

        return Response(
            id = character.id,
            name = character.name,
            description = character.description,
            profileImage = character.profileImage,
            lastUpdatedBy = character.lastUpdatedBy,
            priority = character.priority,
            createdAt = character.createdAt,
            updatedAt = character.updatedAt
        )
    }

    data class Request(
        val novelId: Long,
        val name: String,
        val description: String,
        val profileImage: String?,
        val accountId: Long,
        val priority: Int
    )

    data class Response(
        val id: Long,
        val name: String,
        val description: String,
        val profileImage: String?,
        val lastUpdatedBy: Long?,
        val priority: Int,
        val createdAt: LocalDateTime,
        val updatedAt: LocalDateTime
    )
}
