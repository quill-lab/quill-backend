package lab.ujumeonji.literaturebackend.api.novel

import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.novel.dto.NovelRoomsQueryRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class NovelRoomApiController {

    @GetMapping("/novel-rooms")
    fun getNovelRooms(
        @Valid request: NovelRoomsQueryRequest
    ) {
    }
}
