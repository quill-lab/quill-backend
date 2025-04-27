package lab.ujumeonji.literaturebackend.api.novel

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import lab.ujumeonji.literaturebackend.api.novel.dto.*
import lab.ujumeonji.literaturebackend.domain.novel.command.StoryPhaseEnum
import lab.ujumeonji.literaturebackend.domain.novel.command.UpsertNovelCharacterCommand
import lab.ujumeonji.literaturebackend.support.auth.RequiredAuth
import lab.ujumeonji.literaturebackend.support.validation.ValidUUID
import lab.ujumeonji.literaturebackend.usecase.contributor.ApproveJoinRequestUseCase
import lab.ujumeonji.literaturebackend.usecase.contributor.RejectJoinRequestUseCase
import lab.ujumeonji.literaturebackend.usecase.contributor.RemoveContributorUseCase
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
    private val findChapterTextsUseCase: FindChapterTextsUseCase,
    private val createChapterUseCase: CreateChapterUseCase,
    private val upsertNovelCharactersUseCase: UpsertNovelCharactersUseCase,
    private val updateChapterUseCase: UpdateChapterUseCase,
    private val findDraftChapterTextsUseCase: FindDraftChapterTextsUseCase,
    private val updateDraftChapterTextUseCase: UpdateDraftChapterTextUseCase,
    private val finalizeChapterTextUseCase: FinalizeChapterTextUseCase,
    private val approveJoinRequestUseCase: ApproveJoinRequestUseCase,
    private val rejectJoinRequestUseCase: RejectJoinRequestUseCase,
    private val removeContributorUseCase: RemoveContributorUseCase,
    private val requestChapterPublicationUseCase: RequestChapterPublicationUseCase,
    private val approveChapterPublicationUseCase: ApproveChapterPublicationUseCase,
    private val rejectChapterPublicationUseCase: RejectChapterPublicationUseCase,
    private val getChapterPublicationRequestsUseCase: GetChapterPublicationRequestsUseCase,
) {
    @Operation(summary = "소설 공방 수정", description = "소설 공방을 수정합니다.")
    @PatchMapping("/{novelRoomId}")
    fun updateNovelRoom(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @Valid @RequestBody request: UpdateNovelRequest,
    ): ResponseEntity<UpdateNovelResponse> {
        val result =
            updateNovelUseCase.execute(
                request =
                    UpdateNovelUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        title = request.title,
                        description = request.description,
                        category = request.category,
                        tags = request.tags,
                        synopsis = request.synopsis,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            UpdateNovelResponse(
                id = result.id,
            ),
        )
    }

    @Operation(summary = "소설 공방 조회", description = "소설 공방을 조회합니다.")
    @GetMapping("/{novelRoomId}")
    fun getNovelRoom(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
    ): ResponseEntity<ViewNovelRoomResponse> {
        val result =
            viewJoinedNovelRoomUseCase.execute(
                request =
                    ViewJoinedNovelRoomUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            ViewNovelRoomResponse(
                id = result.id,
                category =
                    ViewNovelRoomResponse.Category(
                        name = result.category.name,
                        alias = result.category.alias,
                    ),
                title = result.title,
                description = result.description,
                synopsis = result.synopsis,
                createdAt = result.createdAt,
                completedAt = result.completedAt,
                role = result.role,
                contributorCount = result.contributorCount,
                maxContributorCount = result.maxContributorCount,
                status = result.status,
                tags = result.tags,
            ),
        )
    }

    @Operation(summary = "소설 공방 생성", description = "소설 공방을 생성합니다.")
    @PostMapping
    fun createNovelRoom(
        @RequiredAuth accountId: String,
        @Valid @RequestBody request: CreateNovelRoomBodyRequest,
    ): ResponseEntity<CreateNovelRoomResponse> {
        val result =
            createNovelRoomUseCase.execute(
                request =
                    CreateNovelRoomUseCase.Request(
                        creatorId = accountId,
                        title = request.title,
                        description = request.description,
                        category = request.category,
                        maxContributorCount = request.maxContributors,
                        novelCoverImage = request.coverImage,
                        synopsis = request.synopsis,
                        tags = request.tags,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CreateNovelRoomResponse(result.novelRoomId))
    }

    @Operation(summary = "소설 등장인물 목록 조회", description = "소설의 등장인물 목록을 조회합니다.")
    @GetMapping("/{novelRoomId}/characters")
    fun getCharacters(
        @PathVariable @ValidUUID novelRoomId: String,
    ): ResponseEntity<List<NovelCharacterResponse>> {
        val result =
            findNovelCharactersUseCase.execute(
                request =
                    FindNovelCharactersUseCase.Request(
                        novelRoomId = novelRoomId,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            result.map {
                NovelCharacterResponse(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    profileImage = it.profileImage,
                    updatedAt = it.updatedAt,
                    updatedBy =
                        it.updatedBy?.let { updatedBy ->
                            NovelCharacterResponse.LastCharacterUpdatedBy(
                                id = updatedBy.id,
                                name = updatedBy.name,
                            )
                        },
                )
            },
        )
    }

    @Operation(summary = "등장인물 추가", description = "소설 공방에 등장인물을 추가합니다.", deprecated = true)
    @PostMapping("/{novelRoomId}/characters")
    fun addCharacter(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @Valid @RequestBody request: AddCharacterRequest,
    ): ResponseEntity<AddCharacterResponse> {
        val result =
            addNovelCharacterUseCase.execute(
                request =
                    AddNovelCharacterUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        name = request.name,
                        description = request.description,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.status(HttpStatus.CREATED).body(
            AddCharacterResponse(
                id = result.id,
            ),
        )
    }

    @Operation(summary = "소설 공방 참여자 목록 조회", description = "소설 공방의 참여자 목록을 조회합니다.")
    @GetMapping("/{novelRoomId}/participants")
    fun getParticipants(
        @PathVariable @ValidUUID novelRoomId: String,
        @RequiredAuth accountId: String,
    ): ResponseEntity<List<NovelRoomParticipantsResponse>> {
        val result =
            findNovelRoomParticipantsUseCase.execute(
                request =
                    FindNovelRoomParticipantsUseCase.Request(
                        accountId = accountId,
                        novelRoomId = novelRoomId,
                    ),
                executedAt = LocalDateTime.now(),
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
            },
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
        val result =
            updateParticipantOrderUseCase.execute(
                request =
                    UpdateParticipantOrderUseCase.Request(
                        accountId = accountId,
                        novelRoomId = novelRoomId,
                        contributorId = participantId,
                        writingOrder = request.writingOrder,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            UpdateParticipantOrderResponse(
                id = result.id,
            ),
        )
    }

    @Operation(summary = "소설 공방 모집글 생성", description = "소설 공방의 모집글을 생성합니다.")
    @PostMapping("/{novelRoomId}/recruitments")
    fun createRecruitment(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @Valid @RequestBody request: CreateNovelRoomRecruitmentRequest,
    ): ResponseEntity<CreateNovelRoomRecruitmentResponse> {
        val result =
            createNovelRoomRecruitmentPostUseCase.execute(
                request =
                    CreateNovelRoomRecruitmentPostUseCase.Request(
                        accountId = accountId,
                        novelRoomId = novelRoomId,
                        title = request.title,
                        content = request.content,
                        link = request.link,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CreateNovelRoomRecruitmentResponse(
                id = result.id,
            ),
        )
    }

    @Operation(summary = "소설 공방의 스토리 아크 목록 조회", description = "소설 공방의 스토리 아크 목록을 조회합니다.")
    @GetMapping("/{novelRoomId}/story-arcs")
    fun findStoryArcs(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
    ): ResponseEntity<List<StoryArcResponse>> {
        val result =
            findNovelStoryArcsUseCase.execute(
                request =
                    FindNovelStoryArcsUseCase.Request(
                        accountId = accountId,
                        novelRoomId = novelRoomId,
                    ),
                executedAt = LocalDateTime.now(),
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
            },
        )
    }

    @Operation(summary = "스토리 아크의 페이즈 수정", description = "스토리 아크의 페이즈를 수정합니다.")
    @PatchMapping("/{novelRoomId}/story-arcs/{phase}")
    fun updateStoryPhase(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable phase: StoryPhaseEnum,
        @Valid @RequestBody request: UpdateStoryPhaseRequest,
    ): ResponseEntity<UpdateStoryPhaseResponse> {
        val result =
            updateStoryPhaseUseCase.execute(
                request =
                    UpdateStoryPhaseUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        phase = phase,
                        description = request.description,
                        startChapterNumber = request.startChapterNumber,
                        endChapterNumber = request.endChapterNumber,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            UpdateStoryPhaseResponse(
                id = result.id,
            ),
        )
    }

    @Operation(summary = "챕터 텍스트 목록 조회", description = "특정 챕터의 텍스트 목록을 조회합니다.")
    @GetMapping("/{novelRoomId}/chapters/{chapterId}/texts")
    fun findChapterTexts(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID chapterId: String,
    ): ResponseEntity<GetChapterTextsResponse> {
        val result =
            findChapterTextsUseCase.execute(
                request =
                    FindChapterTextsUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        chapterId = chapterId,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            GetChapterTextsResponse(
                items =
                    result.items.map { chapterText ->
                        GetChapterTextsResponse.ChapterText(
                            id = chapterText.id,
                            content = chapterText.content,
                            authorName = chapterText.authorName,
                            createdAt = chapterText.createdAt,
                        )
                    },
            ),
        )
    }

    @Operation(summary = "챕터 생성", description = "소설 공방에 새로운 챕터를 생성합니다.")
    @PostMapping("/{novelRoomId}/chapters")
    fun createChapter(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
    ): ResponseEntity<CreateChapterResponse> {
        val result =
            createChapterUseCase.execute(
                request =
                    CreateChapterUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CreateChapterResponse(
                id = result.id,
            ),
        )
    }

    @Operation(summary = "등장인물 생성/수정", description = "소설 공방의 등장인물을 생성하거나 수정합니다.")
    @PutMapping("/{novelRoomId}/characters")
    fun upsertCharacters(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @Valid @RequestBody request: UpsertNovelCharactersRequest,
    ): ResponseEntity<UpsertNovelCharactersResponse> {
        val result =
            upsertNovelCharactersUseCase.execute(
                request =
                    UpsertNovelCharactersUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        characters =
                            request.characters.map { character ->
                                UpsertNovelCharacterCommand(
                                    id = character.id,
                                    name = character.name,
                                    description = character.description,
                                )
                            },
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            UpsertNovelCharactersResponse(
                characters =
                    result.characters.map { character ->
                        UpsertNovelCharactersResponse.CharacterResponse(
                            id = character.id,
                        )
                    },
            ),
        )
    }

    @Operation(summary = "챕터 수정", description = "소설 공방의 특정 챕터 정보를 수정합니다.")
    @PatchMapping("/{novelRoomId}/chapters/{chapterId}")
    fun updateChapter(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID chapterId: String,
        @Valid @RequestBody request: UpdateChapterRequest,
    ): ResponseEntity<UpdateChapterResponse> {
        val result =
            updateChapterUseCase.execute(
                request =
                    UpdateChapterUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        chapterId = chapterId,
                        title = request.title,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            UpdateChapterResponse(
                id = result.id,
            ),
        )
    }

    @Operation(
        summary = "챕터 임시 텍스트 조회",
        description = "특정 챕터의 임시 저장된(DRAFT) 텍스트 목록을 조회합니다. 요청한 사용자가 작성한 임시 텍스트만 조회됩니다.",
    )
    @GetMapping("/{novelRoomId}/chapters/{chapterId}/draft-text")
    fun findDraftChapterText(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID chapterId: String,
    ): ResponseEntity<GetDraftChapterTextResponse> {
        val draftText =
            findDraftChapterTextsUseCase.execute(
                request =
                    FindDraftChapterTextsUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        chapterId = chapterId,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            GetDraftChapterTextResponse(
                id = draftText.id,
                content = draftText.content,
                accountId = draftText.accountId,
                authorName = draftText.authorName,
                createdAt = draftText.createdAt,
                updatedAt = draftText.updatedAt,
            ),
        )
    }

    @Operation(
        summary = "챕터 임시 텍스트 수정",
        description = "특정 챕터의 임시 저장된(DRAFT) 텍스트를 수정합니다. 요청한 사용자가 작성한 임시 텍스트만 수정할 수 있습니다.",
    )
    @PatchMapping("/{novelRoomId}/chapters/{chapterId}/draft-text")
    fun updateDraftChapterText(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID chapterId: String,
        @Valid @RequestBody request: UpdateDraftChapterTextRequest,
    ): ResponseEntity<Void> {
        val result =
            updateDraftChapterTextUseCase.execute(
                request =
                    UpdateDraftChapterTextUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        chapterId = chapterId,
                        content = request.content,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "소설 공방 참여 신청 승인", description = "소설 공방 관리자가 작가 참여 신청을 승인합니다.")
    @PostMapping("/{novelRoomId}/join-requests/{requesterId}/approve")
    fun approveJoinRequest(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID requesterId: String,
    ): ResponseEntity<Void> {
        approveJoinRequestUseCase.execute(
            request =
                ApproveJoinRequestUseCase.Request(
                    adminAccountId = accountId,
                    novelRoomId = novelRoomId,
                    requesterAccountId = requesterId,
                ),
            executedAt = LocalDateTime.now(),
        )

        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "소설 공방 참여 신청 거부", description = "소설 공방 관리자가 작가 참여 신청을 거부합니다.")
    @PostMapping("/{novelRoomId}/join-requests/{requesterId}/reject")
    fun rejectJoinRequest(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID requesterId: String,
    ): ResponseEntity<Void> {
        rejectJoinRequestUseCase.execute(
            request =
                RejectJoinRequestUseCase.Request(
                    adminAccountId = accountId,
                    novelRoomId = novelRoomId,
                    requesterAccountId = requesterId,
                ),
            executedAt = LocalDateTime.now(),
        )

        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "소설 공방 참여자 강제 퇴장", description = "소설 공방 관리자가 참여자를 강제로 퇴장시킵니다.")
    @DeleteMapping("/{novelRoomId}/contributors/{contributorId}")
    fun removeContributor(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID contributorId: String,
    ): ResponseEntity<Void> {
        val result =
            removeContributorUseCase.execute(
                request =
                    RemoveContributorUseCase.Request(
                        adminAccountId = accountId,
                        novelRoomId = novelRoomId,
                        targetAccountId = contributorId,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return if (result.success) {
            ResponseEntity.noContent().build()
        } else {
            ResponseEntity.badRequest().build()
        }
    }

    @Operation(
        summary = "챕터 텍스트 확정 및 다음 작가로 넘기기",
        description = "현재 순서의 작가가 작성 중인 챕터 텍스트를 확정하고, 다음 순서의 작가로 넘깁니다.",
    )
    @PostMapping("/{novelRoomId}/chapters/{chapterId}/finalize")
    fun finalizeChapterText(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID chapterId: String,
    ): ResponseEntity<FinalizeChapterTextResponse> {
        val result =
            finalizeChapterTextUseCase.execute(
                request =
                    FinalizeChapterTextUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        chapterId = chapterId,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            FinalizeChapterTextResponse(
                success = result.success,
            ),
        )
    }

    @Operation(
        summary = "챕터 연재 신청",
        description = "챕터의 연재를 신청합니다. 대표 작가만 신청할 수 있습니다.",
    )
    @PostMapping("/{novelRoomId}/chapters/{chapterId}/publication-requests")
    fun requestChapterPublication(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID chapterId: String,
    ): ResponseEntity<RequestChapterPublicationResponse> {
        val result =
            requestChapterPublicationUseCase.execute(
                request =
                    RequestChapterPublicationUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        chapterId = chapterId,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            RequestChapterPublicationResponse(
                id = result.id,
            ),
        )
    }

    @Operation(
        summary = "챕터 연재 신청 승인",
        description = "챕터의 연재 신청을 승인합니다. 관리자만 승인할 수 있습니다.",
    )
    @PostMapping("/{novelRoomId}/chapters/{chapterId}/publication-requests/approve")
    fun approveChapterPublication(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID chapterId: String,
    ): ResponseEntity<ApproveChapterPublicationResponse> {
        val result =
            approveChapterPublicationUseCase.execute(
                request =
                    ApproveChapterPublicationUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        chapterId = chapterId,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            ApproveChapterPublicationResponse(
                id = result.id,
            ),
        )
    }

    @Operation(
        summary = "챕터 연재 신청 거부",
        description = "챕터의 연재 신청을 거부합니다. 관리자만 거부할 수 있습니다.",
    )
    @PostMapping("/{novelRoomId}/chapters/{chapterId}/publication-requests/reject")
    fun rejectChapterPublication(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID chapterId: String,
    ): ResponseEntity<RejectChapterPublicationResponse> {
        val result =
            rejectChapterPublicationUseCase.execute(
                request =
                    RejectChapterPublicationUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        chapterId = chapterId,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            RejectChapterPublicationResponse(
                id = result.id,
            ),
        )
    }

    @Operation(
        summary = "챕터 연재 신청 내역 조회",
        description = "챕터의 연재 신청 내역을 조회합니다.",
    )
    @GetMapping("/{novelRoomId}/chapters/{chapterId}/publication-requests")
    fun getChapterPublicationRequests(
        @RequiredAuth accountId: String,
        @PathVariable @ValidUUID novelRoomId: String,
        @PathVariable @ValidUUID chapterId: String,
    ): ResponseEntity<GetChapterPublicationRequestsResponse> {
        val result =
            getChapterPublicationRequestsUseCase.execute(
                request =
                    GetChapterPublicationRequestsUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = novelRoomId,
                        chapterId = chapterId,
                    ),
                executedAt = LocalDateTime.now(),
            )

        return ResponseEntity.ok(
            GetChapterPublicationRequestsResponse(
                requests =
                    result.requests.map { request ->
                        GetChapterPublicationRequestsResponse.ChapterPublicationRequest(
                            id = request.id,
                            chapterId = request.chapterId,
                            requesterId = request.requesterId,
                            status = request.status,
                            reviewerId = request.reviewerId,
                            comment = request.comment,
                            reviewedAt = request.reviewedAt,
                            createdAt = request.createdAt,
                            updatedAt = request.updatedAt,
                        )
                    },
            ),
        )
    }
}
