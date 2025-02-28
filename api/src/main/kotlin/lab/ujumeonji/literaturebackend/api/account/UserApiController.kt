package lab.ujumeonji.literaturebackend.api.account

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.account.dto.ContributorRequestsQueryRequest
import lab.ujumeonji.literaturebackend.api.account.dto.ContributorRequestsResponse
import lab.ujumeonji.literaturebackend.api.account.dto.JoinedNovelRoomsQueryRequest
import lab.ujumeonji.literaturebackend.api.novel.dto.JoinedNovelRoomsResponse
import lab.ujumeonji.literaturebackend.domain.contributor.command.NovelRoomSortType
import lab.ujumeonji.literaturebackend.support.auth.RequiredAuth
import lab.ujumeonji.literaturebackend.usecase.contributor.FindContributorRequestsUseCase
import lab.ujumeonji.literaturebackend.usecase.novel.FindJoinedNovelRoomsUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@Tag(name = "User", description = "사용자 API")
@RestController
@RequestMapping("/api/v1/users")
class UserApiController(
    private val findJoinedNovelRoomsUseCase: FindJoinedNovelRoomsUseCase,
    private val findContributorRequestsUseCase: FindContributorRequestsUseCase,
) {
    @Operation(summary = "참여한 소설 공방 목록 조회", description = "사용자가 참여한 소설 공방 목록을 조회합니다.")
    @GetMapping("/me/novel-rooms")
    fun getJoinedNovelRooms(
        @RequiredAuth accountId: String,
        @Valid request: JoinedNovelRoomsQueryRequest
    ): ResponseEntity<JoinedNovelRoomsResponse> {
        val result = findJoinedNovelRoomsUseCase.execute(
            request = FindJoinedNovelRoomsUseCase.Request(
                accountId = accountId,
                page = request.page,
                size = request.size,
                sort = NovelRoomSortType.from(request.sort),
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

    @Operation(summary = "소설 공방 참여 신청 목록 조회", description = "사용자가 신청한 소설 공방 참여 신청 목록을 조회합니다.")
    @GetMapping("/me/contributor-requests")
    fun getContributorRequestHistories(
        @RequiredAuth accountId: String,
        @Valid request: ContributorRequestsQueryRequest
    ): ResponseEntity<ContributorRequestsResponse> {
        val result = findContributorRequestsUseCase.execute(
            request = FindContributorRequestsUseCase.Request(
                accountId = accountId,
                page = request.page,
                size = request.size,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(
            ContributorRequestsResponse(
                items = result.result.map { item ->
                    ContributorRequestsResponse.ResponseItem(
                        id = item.id,
                        title = item.title,
                        requestedAt = item.requestedAt,
                        joinedAt = item.joinedAt,
                        leftAt = item.leftAt,
                        status = item.status.name,
                    )
                },
                totalCount = result.totalCount,
                size = result.size,
                page = result.page
            )
        )
    }
}
