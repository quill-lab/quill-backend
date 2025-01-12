package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.service.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class GetNovelCharactersUseCase(
    private val novelService: NovelService,
) : UseCase<GetNovelCharactersUseCase.Request, List<GetNovelCharactersUseCase.Response>> {

    override fun execute(request: Request): List<Response> {
        val novel = novelService.findNovel(request.novelId) ?: throw IllegalArgumentException("Novel not found")

        return novel.characters.map { character ->
            Response(
                name = character.name,
                description = character.description,
                profileImage = character.profileImage
            )
        }
    }

    data class Request(
        val novelId: Long,
        val executedAt: LocalDateTime
    )

    data class Response(
        val name: String,
        val description: String,
        val profileImage: String
    )
}
