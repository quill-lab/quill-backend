package lab.ujumeonji.literaturebackend.api.novel

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.novel.dto.*
import lab.ujumeonji.literaturebackend.domain.novel.command.StoryPhaseEnum
import lab.ujumeonji.literaturebackend.support.auth.RequiredAuth
import lab.ujumeonji.literaturebackend.support.validation.ValidUUID
import lab.ujumeonji.literaturebackend.usecase.novel.*
import lab.ujumeonji.literaturebackend.usecase.post.CreateNovelRoomRecruitmentPostUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@Tag(name = "Novel Room", description = "소설 공방 API")
@RestController
@RequestMapping("/api/v1/novel-rooms")
@Validated
class NovelRoomApiController(
    private val createNovelRoomUseCase: CreateNovelRoomUseCase,
    private val viewJoinedNovelRoomUseCase: ViewJoinedNovelRoomUseCase,
    private val updateNovelUseCase: UpdateNovelUseCase,
    private val findNovelCharactersUseCase: FindNovelCharactersUseCase,
    private val addNovelCharacterUseCase: AddNovelCharacterUseCase,
    private val findNovelRoomParticipantsUseCase: FindNovelRoomParticipantsUseCase,
    private val updateParticipantOrderUseCase: UpdateParticipantOrderUseCase,
    private val createNovelRoomRecruitmentPostUseCase: CreateNovelRoomRecruitmentPostUseCase,
    private val findNovelStoryArcsUseCase: FindNovelStoryArcsUseCase,
    private val updateStoryPhaseUseCase: UpdateStoryPhaseUseCase,
    private val writeChapterTextUseCase: WriteChapterTextUseCase,
    private val findChapterTextsUseCase: FindChapterTextsUseCase
) {

    @Operation(summary = "소설 공방 수정", description = "소설 공방을 수정합니다.")
    @PatchMapping("/{novelRoomId}")
    fun updateNovelRoom(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
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
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
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
                description = result.description,
                synopsis = result.synopsis,
                createdAt = result.createdAt,
                completedAt = result.completedAt,
                role = result.role,
                contributorCount = result.contributorCount,
                maxContributorCount = result.maxContributorCount,
                author = result.author?.let {
                    ViewNovelRoomResponse.Author(
                        id = it.id,
                        name = it.name
                    )
                },
                status = result.status,
                tags = result.tags
            )
        )
    }

    @Operation(summary = "소설 공방 생성", description = "소설 공방을 생성합니다.")
    @PostMapping
    fun createNovelRoom(
        @RequiredAuth accountId: String,
        @Valid @RequestBody request: CreateNovelRoomBodyRequest
    ): ResponseEntity<CreateNovelRoomResponse> {
        val result = createNovelRoomUseCase.execute(
            request = CreateNovelRoomUseCase.Request(
                creatorId = accountId,
                title = request.title,
                description = request.description,
                category = request.category,
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

    @Operation(summary = "소설 등장인물 목록 조회", description = "소설의 등장인물 목록을 조회합니다.")
    @GetMapping("/{novelRoomId}/characters")
    fun getCharacters(
        @PathVariable @ValidUUID novelRoomId: String,
    ): ResponseEntity<List<NovelCharacterResponse>> {
        val result = findNovelCharactersUseCase.execute(
            request = FindNovelCharactersUseCase.Request(
                novelRoomId = novelRoomId,
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
                    }
                )
            }
        )
    }

    @Operation(summary = "캐릭터 추가", description = "소설 공방에 캐릭터를 추가합니다.")
    @PostMapping("/{novelRoomId}/characters")
    fun addCharacter(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @Valid @RequestBody request: AddCharacterRequest
    ): ResponseEntity<AddCharacterResponse> {
        val result = addNovelCharacterUseCase.execute(
            request = AddNovelCharacterUseCase.Request(
                accountId = accountId,
                contributorGroupId = novelRoomId,
                name = request.name,
                description = request.description,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(
            AddCharacterResponse(
                id = result.id,
            )
        )
    }

    @Operation(summary = "소설 공방 참여자 목록 조회", description = "소설 공방의 참여자 목록을 조회합니다.")
    @GetMapping("/{novelRoomId}/participants")
    fun getParticipants(
        @PathVariable @ValidUUID novelRoomId: String,
        @RequiredAuth accountId: String,
    ): ResponseEntity<List<NovelRoomParticipantsResponse>> {
        val result = findNovelRoomParticipantsUseCase.execute(
            request = FindNovelRoomParticipantsUseCase.Request(
                accountId = accountId,
                novelRoomId = novelRoomId,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(
            result.map { response ->
                NovelRoomParticipantsResponse(
                    id = response.id,
                    nickname = response.nickname,
                    role = response.role,
                    writingOrder = response.writingOrder,
                    joinedAt = response.joinedAt,
                )
            }
        )
    }

    @Operation(summary = "소설 공방 참여자 작성 순서 변경", description = "소설 공방의 참여자 작성 순서를 변경합니다.")
    @PatchMapping("/{novelRoomId}/participants/{participantId}/order")
    fun updateParticipantOrder(
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID participantId: String,
        @Valid @RequestBody request: UpdateParticipantOrderRequest,
        @RequiredAuth accountId: String,
    ): ResponseEntity<UpdateParticipantOrderResponse> {
        val result = updateParticipantOrderUseCase.execute(
            request = UpdateParticipantOrderUseCase.Request(
                accountId = accountId,
                novelRoomId = novelRoomId,
                contributorId = participantId,
                writingOrder = request.writingOrder,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(
            UpdateParticipantOrderResponse(
                id = result.id,
            )
        )
    }

    @Operation(summary = "소설 공방 모집글 생성", description = "소설 공방의 모집글을 생성합니다.")
    @PostMapping("/{novelRoomId}/recruitments")
    fun createRecruitment(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @Valid @RequestBody request: CreateNovelRoomRecruitmentRequest
    ): ResponseEntity<CreateNovelRoomRecruitmentResponse> {
        val result = createNovelRoomRecruitmentPostUseCase.execute(
            request = CreateNovelRoomRecruitmentPostUseCase.Request(
                accountId = accountId,
                novelRoomId = novelRoomId,
                title = request.title,
                content = request.content,
                link = request.link,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CreateNovelRoomRecruitmentResponse(
                id = result.id,
            )
        )
    }

    @Operation(summary = "소설 공방의 스토리 아크 목록 조회", description = "소설 공방의 스토리 아크 목록을 조회합니다.")
    @GetMapping("/{novelRoomId}/story-arcs")
    fun findStoryArcs(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String
    ): ResponseEntity<List<StoryArcResponse>> {
        val result = findNovelStoryArcsUseCase.execute(
            request = FindNovelStoryArcsUseCase.Request(
                accountId = accountId,
                novelRoomId = novelRoomId
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(
            result.map {
                StoryArcResponse(
                    id = it.id,
                    description = it.description,
                    phase = it.phase,
                    phaseAlias = it.phase.koreanName,
                    firstChapterNumber = it.firstChapterNumber,
                    lastChapterNumber = it.lastChapterNumber,
                    lastModifiedAt = it.lastModifiedAt,
                )
            }
        )
    }

    @Operation(summary = "스토리 아크의 페이즈 수정", description = "스토리 아크의 페이즈를 수정합니다.")
    @PatchMapping("/{novelRoomId}/story-arcs/{phase}")
    fun updateStoryPhase(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable phase: StoryPhaseEnum,
        @Valid @RequestBody request: UpdateStoryPhaseRequest
    ): ResponseEntity<UpdateStoryPhaseResponse> {
        val result = updateStoryPhaseUseCase.execute(
            request = UpdateStoryPhaseUseCase.Request(
                accountId = accountId,
                contributorGroupId = novelRoomId,
                phase = phase,
                description = request.description,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(
            UpdateStoryPhaseResponse(
                id = result.id,
            )
        )
    }

    @Operation(summary = "챕터 텍스트 작성", description = "특정 챕터에 텍스트를 작성합니다.")
    @PostMapping("/{novelRoomId}/chapters/{chapterId}/texts")
    fun createChapterText(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID chapterId: String,
        @Valid @RequestBody request: WriteChapterTextRequest
    ): ResponseEntity<WriteChapterTextResponse> {
        val result = writeChapterTextUseCase.execute(
            request = WriteChapterTextUseCase.Request(
                accountId = accountId,
                chapterId = chapterId,
                content = request.content,
                contributorGroupId = novelRoomId,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(
            WriteChapterTextResponse(
                id = result.id
            )
        )
    }

    @Operation(summary = "챕터 텍스트 목록 조회", description = "특정 챕터의 텍스트 목록을 조회합니다.")
    @GetMapping("/{novelRoomId}/chapters/{chapterId}/texts")
    fun findChapterTexts(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID chapterId: String
    ): ResponseEntity<GetChapterTextsResponse> {
        val result = findChapterTextsUseCase.execute(
            request = FindChapterTextsUseCase.Request(
                accountId = accountId,
                contributorGroupId = novelRoomId,
                chapterId = chapterId,
            ),
            executedAt = LocalDateTime.now()
        )

        return ResponseEntity.ok(
            GetChapterTextsResponse(
                items = result.items.map { chapterText ->
                    GetChapterTextsResponse.ChapterText(
                        id = chapterText.id,
                        content = chapterText.content,
                        authorName = chapterText.authorName,
                        createdAt = chapterText.createdAt
                    )
                }
            )
        )
    }
}
