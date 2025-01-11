package lab.ujumeonji.literaturebackend.api.novel

import lab.ujumeonji.literaturebackend.api.novel.dto.ListNovelCategoriesResponse
import lab.ujumeonji.literaturebackend.domain.novel.NovelCategory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/novels")
class NovelApiController {

    @GetMapping("/categories")
    fun getCategories(): List<ListNovelCategoriesResponse> =
        NovelCategory.entries.map { category ->
            ListNovelCategoriesResponse(
                name = category.name,
                alias = category.alias
            )
        }
}
