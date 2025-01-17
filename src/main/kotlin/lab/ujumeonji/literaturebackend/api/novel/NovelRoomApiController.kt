package lab.ujumeonji.literaturebackend.api.novel

import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.novel.dto.CreateNovelRoomBodyRequest
import lab.ujumeonji.literaturebackend.api.novel.dto.CreateNovelRoomRecruitmentRequest
import lab.ujumeonji.literaturebackend.api.novel.dto.CreateNovelRoomResponse
import lab.ujumeonji.literaturebackend.api.novel.dto.NovelRoomsQueryRequest
import lab.ujumeonji.literaturebackend.support.auth.RequiredAuth
import lab.ujumeonji.literaturebackend.support.http.ApiResponse
import lab.ujumeonji.literaturebackend.usecase.novel.CreateNovelRoomUseCase
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/novel-rooms")
class NovelRoomApiController(
    private val createNovelRoomUseCase: CreateNovelRoomUseCase,
) {

    @GetMapping
    fun getNovelRooms(
        @Valid request: NovelRoomsQueryRequest
    ) {
        // TODO: Implement novel room query logic
    }

    @PostMapping
    fun createNovelRoom(
        @RequiredAuth userId: Long,
        @Valid request: CreateNovelRoomBodyRequest
    ): ApiResponse<CreateNovelRoomResponse> {
        val result = createNovelRoomUseCase.execute(
            request = CreateNovelRoomUseCase.Request(
                creatorId = userId,
                title = request.title,
                description = request.description,
                maxContributorCount = request.maxContributors,
                novelCoverImage = request.coverImage,
                tags = request.tags,
            ),
            executedAt = LocalDateTime.now()
        )

        return ApiResponse(HttpStatus.CREATED.value(), "", CreateNovelRoomResponse(result.novelRoomId))
    }

    @PostMapping("/{novelRoomId}/recruitments")
    fun createRecruitment(
        @PathVariable novelRoomId: Long,
        @Valid @RequestBody request: CreateNovelRoomRecruitmentRequest
    ) {
        // TODO: Implement recruitment creation logic
    }
}
