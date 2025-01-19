package lab.ujumeonji.literaturebackend.api.novel

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import lab.ujumeonji.literaturebackend.api.novel.dto.NovelCategoriesResponse
import lab.ujumeonji.literaturebackend.api.novel.dto.NovelCharacterResponse
import lab.ujumeonji.literaturebackend.api.novel.dto.UpdateNovelRequest
import lab.ujumeonji.literaturebackend.domain.novel.NovelCategory
import lab.ujumeonji.literaturebackend.usecase.novel.FindNovelCharactersUseCase
import lab.ujumeonji.literaturebackend.usecase.novel.UpdateNovelUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Tag(name = "Novel", description = "소설 API")
@RestController
@RequestMapping("/api/v1/novels")
class NovelApiController(
    private val findNovelCharactersUseCase: FindNovelCharactersUseCase,
    private val updateNovelUseCase: UpdateNovelUseCase
) {

    @Operation(summary = "소설 정보 수정", description = "소설 정보를 수정합니다.")
    @PatchMapping("/{novelRoomId}")
    fun updateNovel(
        @PathVariable novelRoomId: Long,
        @RequestBody request: UpdateNovelRequest,
        @RequestAttribute accountId: Long
    ): ResponseEntity<Unit> {
        updateNovelUseCase.execute(
            request = UpdateNovelUseCase.Request(
                accountId = accountId,
                contributorGroupId = novelRoomId,
                title = request.title,
                description = request.description,
                category = request.category,
                tags = request.tags,
                synopsis = request.synopsis
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "소설 카테고리 목록 조회", description = "소설 카테고리 목록을 조회합니다.")
    @GetMapping("/categories")
    fun getCategories(): ResponseEntity<List<NovelCategoriesResponse>> =
        ResponseEntity.ok(
            NovelCategory.entries.map { category ->
                NovelCategoriesResponse(
                    name = category.name,
                    alias = category.alias
                )
            }
        )

    @Operation(summary = "소설 등장인물 목록 조회", description = "소설의 등장인물 목록을 조회합니다.")
    @GetMapping("/{novelId}/characters")
    fun getCharacters(
        @PathVariable novelId: Long,
    ): ResponseEntity<List<NovelCharacterResponse>> {
        val result = findNovelCharactersUseCase.execute(
            request = FindNovelCharactersUseCase.Request(
                novelId = novelId,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(
            result.map {
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
                    })
            })
    }
}
