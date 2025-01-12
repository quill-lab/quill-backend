package lab.ujumeonji.literaturebackend.api.novel

import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.novel.dto.CreateNovelRoomBodyRequest
import lab.ujumeonji.literaturebackend.api.novel.dto.CreateNovelRoomRecruitmentRequest
import lab.ujumeonji.literaturebackend.api.novel.dto.CreateNovelRoomResponse
import lab.ujumeonji.literaturebackend.api.novel.dto.NovelRoomsQueryRequest
import lab.ujumeonji.literaturebackend.common.auth.RequiredAuth
import lab.ujumeonji.literaturebackend.usecase.novel.CreateNovelRoomUseCase
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
    ): CreateNovelRoomResponse {
        val result = createNovelRoomUseCase.execute(
            CreateNovelRoomUseCase.Request(
                creatorId = userId,
                title = request.title,
                description = request.description,
                maxContributorCount = request.maxContributors,
                novelCoverImage = request.coverImage,
                executedAt = LocalDateTime.now()
            )
        )

        return CreateNovelRoomResponse(result.novelRoomId)
    }

    @PostMapping("/{novelRoomId}/recruitments")
    fun createRecruitment(
        @PathVariable novelRoomId: Long,
        @Valid @RequestBody request: CreateNovelRoomRecruitmentRequest
    ) {
        // TODO: Implement recruitment creation logic
    }
}
