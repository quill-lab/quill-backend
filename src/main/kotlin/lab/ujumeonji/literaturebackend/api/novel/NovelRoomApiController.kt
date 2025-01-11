package lab.ujumeonji.literaturebackend.api.novel

import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.novel.dto.CreateNovelRoomBodyRequest
import lab.ujumeonji.literaturebackend.api.novel.dto.CreateNovelRoomRecruitmentRequest
import lab.ujumeonji.literaturebackend.api.novel.dto.NovelRoomsQueryRequest
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/novel-rooms")
class NovelRoomApiController {

    @GetMapping
    fun getNovelRooms(
        @Valid request: NovelRoomsQueryRequest
    ) {
        // TODO: Implement novel room query logic
    }

    @PostMapping
    fun createNovelRoom(
        @Valid request: CreateNovelRoomBodyRequest
    ) {
        // TODO: Implement novel room creation logic
    }

    @PostMapping("/{novelRoomId}/recruitments")
    fun createRecruitment(
        @PathVariable novelRoomId: Long,
        @Valid @RequestBody request: CreateNovelRoomRecruitmentRequest
    ) {
        // TODO: Implement recruitment creation logic
    }
}
