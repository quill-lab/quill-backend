package lab.ujumeonji.literaturebackend.api.novel

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.novel.dto.*
import lab.ujumeonji.literaturebackend.support.auth.RequiredAuth
import lab.ujumeonji.literaturebackend.usecase.novel.CreateNovelRoomUseCase
import lab.ujumeonji.literaturebackend.usecase.novel.FindJoinedNovelRoomsUseCase
import lab.ujumeonji.literaturebackend.usecase.novel.UpdateNovelUseCase
import lab.ujumeonji.literaturebackend.usecase.novel.ViewJoinedNovelRoomUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Tag(name = "Novel Room", description = "소설 공방 API")
@RestController
@RequestMapping("/api/v1/novel-rooms")
class NovelRoomApiController(
    private val createNovelRoomUseCase: CreateNovelRoomUseCase,
    private val findJoinedNovelRoomsUseCase: FindJoinedNovelRoomsUseCase,
    private val viewJoinedNovelRoomUseCase: ViewJoinedNovelRoomUseCase,
    private val updateNovelUseCase: UpdateNovelUseCase,
) {

    @Operation(summary = "소설 공방 수정", description = "소설 공방을 수정합니다.")
    @PatchMapping("/{novelRoomId}")
    fun updateNovelRoom(
        @RequiredAuth accountId: Long,
        @PathVariable novelRoomId: Long,
        @Valid @RequestBody request: UpdateNovelRequest
    ): ResponseEntity<UpdateNovelResponse> {
        val result = updateNovelUseCase.execute(
            request = UpdateNovelUseCase.Request(
                accountId = accountId,
                contributorGroupId = novelRoomId,
                title = request.title,
                description = request.description,
                category = request.category,
                tags = request.tags,
                synopsis = request.synopsis,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(
            UpdateNovelResponse(
                id = result.id,
            )
        )
    }

    @Operation(summary = "소설 공방 조회", description = "소설 공방을 조회합니다.")
    @GetMapping("/{novelRoomId}")
    fun getNovelRoom(
        @RequiredAuth accountId: Long,
        @PathVariable novelRoomId: Long,
    ): ResponseEntity<ViewNovelRoomResponse> {
        val result = viewJoinedNovelRoomUseCase.execute(
            request = ViewJoinedNovelRoomUseCase.Request(
                accountId = accountId,
                contributorGroupId = novelRoomId
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(
            ViewNovelRoomResponse(
                id = result.id,
                category = ViewNovelRoomResponse.Category(
                    name = result.category.name,
                    alias = result.category.alias
                ),
                title = result.title,
                createdAt = result.createdAt,
                completedAt = result.completedAt,
                role = result.role,
                contributorCount = result.contributorCount,
                maxContributorCount = result.maxContributorCount,
                author = ViewNovelRoomResponse.Author(
                    id = result.author.id,
                    name = result.author.name
                ),
                status = result.status
            )
        )
    }

    @Operation(summary = "소설 공방 목록 조회", description = "소설 공방 목록을 조회합니다.")
    @GetMapping
    fun getNovelRooms(
        @RequiredAuth accountId: Long,
        @Valid request: JoinedNovelRoomsQueryRequest
    ): ResponseEntity<JoinedNovelRoomsResponse> {
        val result = findJoinedNovelRoomsUseCase.execute(
            request = FindJoinedNovelRoomsUseCase.Request(
                accountId = accountId,
                page = request.page,
                size = request.size
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(
            JoinedNovelRoomsResponse(
                items = result.result.map { item ->
                    JoinedNovelRoomsResponse.ResponseItem(
                        id = item.id,
                        category = JoinedNovelRoomsResponse.ResponseItem.Category(
                            name = item.category.name,
                            alias = item.category.alias
                        ),
                        title = item.title,
                        createdAt = item.createdAt,
                        completedAt = item.completedAt,
                        role = item.role,
                        contributorCount = item.contributorCount,
                        maxContributorCount = item.maxContributorCount,
                        author = item.currentAuthor?.let {
                            JoinedNovelRoomsResponse.ResponseItem.Author(
                                id = it.id,
                                name = it.name
                            )
                        },
                        status = item.status
                    )
                },
                totalCount = result.totalCount,
                size = result.size,
                page = result.page
            )
        )
    }

    @Operation(summary = "소설 공방 생성", description = "소설 공방을 생성합니다.")
    @PostMapping
    fun createNovelRoom(
        @RequiredAuth accountId: Long,
        @Valid @RequestBody request: CreateNovelRoomBodyRequest
    ): ResponseEntity<CreateNovelRoomResponse> {
        val result = createNovelRoomUseCase.execute(
            request = CreateNovelRoomUseCase.Request(
                creatorId = accountId,
                title = request.title,
                description = request.description,
                maxContributorCount = request.maxContributors,
                novelCoverImage = request.coverImage,
                synopsis = request.synopsis,
                tags = request.tags,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CreateNovelRoomResponse(result.novelRoomId))
    }

    @Operation(summary = "소설 공방 모집글 생성", description = "소설 공방의 모집글을 생성합니다.")
    @PostMapping("/{novelRoomId}/recruitments")
    fun createRecruitment(
        @PathVariable novelRoomId: Long,
        @Valid @RequestBody request: CreateNovelRoomRecruitmentRequest
    ) {
        // TODO: Implement recruitment creation logic
    }
}
