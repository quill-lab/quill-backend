package lab.ujumeonji.literaturebackend.api.novel

import lab.ujumeonji.literaturebackend.api.novel.dto.NovelCategoriesResponse
import lab.ujumeonji.literaturebackend.api.novel.dto.NovelCharacterResponse
import lab.ujumeonji.literaturebackend.domain.novel.NovelCategory
import lab.ujumeonji.literaturebackend.support.http.ApiResponse
import lab.ujumeonji.literaturebackend.usecase.novel.GetNovelCharactersUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/novels")
class NovelApiController(
    private val getNovelCharactersUseCase: GetNovelCharactersUseCase
) {

    @GetMapping("/categories")
    fun getCategories(): ApiResponse<List<NovelCategoriesResponse>> =
        ApiResponse(
            HttpStatus.OK.value(), "",
            NovelCategory.entries.map { category ->
                NovelCategoriesResponse(
                    name = category.name,
                    alias = category.alias
                )
            })

    @GetMapping("/{novelId}/characters")
    fun getCharacters(
        @PathVariable novelId: Long,
    ): ApiResponse<List<NovelCharacterResponse>> {
        val result = getNovelCharactersUseCase.execute(
            request = GetNovelCharactersUseCase.Request(
                novelId = novelId,
            ),
            executedAt = LocalDateTime.now()
        )

        return ApiResponse(HttpStatus.OK.value(), "", result.map {
            NovelCharacterResponse(
                id = it.id,
                name = it.name,
                description = it.description,
                profileImage = it.profileImage,
                updatedAt = it.updatedAt,
                updatedBy = it.updatedBy?.let { updatedBy ->
                    NovelCharacterResponse.LastCharacterUpdatedBy(
                        id = updatedBy.id,
                        name = updatedBy.name
                    )
                }
            )
        })
    }
}
