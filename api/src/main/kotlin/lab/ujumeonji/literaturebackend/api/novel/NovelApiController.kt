package lab.ujumeonji.literaturebackend.api.novel

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lab.ujumeonji.literaturebackend.api.novel.dto.NovelCategoriesResponse
import lab.ujumeonji.literaturebackend.domain.novel.command.NovelCategoryEnum
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Novel", description = "소설 API")
@RestController
@RequestMapping("/api/v1/novels")
class NovelApiController {

    @Operation(summary = "소설 카테고리 목록 조회", description = "소설 카테고리 목록을 조회합니다.")
    @GetMapping("/categories")
    fun getCategories(): ResponseEntity<List<NovelCategoriesResponse>> =
        ResponseEntity.ok(
            NovelCategoryEnum.entries.map { category ->
                NovelCategoriesResponse(
                    name = category.name,
                    alias = category.alias
                )
            }
        )
}
