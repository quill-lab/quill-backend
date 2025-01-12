package lab.ujumeonji.literaturebackend.api.novel

import lab.ujumeonji.literaturebackend.api.novel.dto.NovelCategoriesResponse
import lab.ujumeonji.literaturebackend.api.novel.dto.NovelCharacterResponse
import lab.ujumeonji.literaturebackend.domain.novel.NovelCategory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/novels")
class NovelApiController {

    @GetMapping("/categories")
    fun getCategories(): List<NovelCategoriesResponse> =
        NovelCategory.entries.map { category ->
            NovelCategoriesResponse(
                name = category.name,
                alias = category.alias
            )
        }

    @GetMapping("/{novelId}/characters")
    fun getCharacters(): List<NovelCharacterResponse> =
        NovelCategory.entries.map { category ->
            NovelCategoriesResponse(
                name = category.name,
                alias = category.alias
            )
        }
}
