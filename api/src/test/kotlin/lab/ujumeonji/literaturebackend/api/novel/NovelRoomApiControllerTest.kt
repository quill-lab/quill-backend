package lab.ujumeonji.literaturebackend.api.novel

import com.fasterxml.jackson.databind.ObjectMapper
import lab.ujumeonji.literaturebackend.domain.novel.command.StoryPhaseEnum
import lab.ujumeonji.literaturebackend.support.AuthControllerTest
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class NovelRoomApiControllerTest
@Autowired
constructor(
    mockMvc: MockMvc,
    objectMapper: ObjectMapper,
) : AuthControllerTest(
    mockMvc = mockMvc,
    objectMapper = objectMapper,
) {
    init {
        given("대표 작가가 챕터 생성을 요청할 때") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)
            val dummyBody = emptyMap<String, String>()

            `when`("인증된 사용자가 챕터 생성을 요청하면") {
                val response = performAuthPost("/api/v1/novel-rooms/$novelRoomId/chapters", dummyBody, account)

                then("새로운 챕터가 생성된다") {
                    response
                        .andExpect(status().isCreated)
                        .andExpect(jsonPath("$.id").exists())
                }
            }

            `when`("인증되지 않은 사용자가 챕터 생성을 요청하면") {
                val response = performPost("/api/v1/novel-rooms/$novelRoomId/chapters")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }

            `when`("존재하지 않는 소설 공방에 챕터 생성을 요청하면") {
                val invalidNovelRoomId = "00000000-0000-0000-0000-000000000000"
                val response = performAuthPost("/api/v1/novel-rooms/$invalidNovelRoomId/chapters", account)

                then("소설 공방을 찾을 수 없다는 오류가 발생한다") {
                    response
                        .andExpect(status().isNotFound)
                        .andExpect(jsonPath("$.code").value(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND.code))
                }
            }
        }

        given("유효한 소설 공방 생성 요청이 주어지고") {
            val request =
                mapOf(
                    "maxContributors" to 5,
                    "title" to "테스트 소설",
                    "description" to "테스트 소설 설명",
                    "category" to "GENERAL",
                    "tags" to listOf("태그1", "태그2"),
                    "synopsis" to "테스트 소설의 줄거리입니다.",
                    "coverImage" to "https://example.com/cover.jpg",
                )

            `when`("인증된 사용자가 소설 공방 생성을 요청하면") {
                val response = performAuthPost("/api/v1/novel-rooms", request)

                then("소설 공방이 생성된다") {
                    response
                        .andExpect(status().isCreated)
                        .andExpect(jsonPath("$.id").exists())
                }
            }

            `when`("인증되지 않은 사용자가 소설 공방 생성을 요청하면") {
                val response = performPost("/api/v1/novel-rooms", request)

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }

        given("대표 작가가 유효한 등장인물 정보로") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            val request =
                mapOf(
                    "name" to "테스트 캐릭터",
                    "description" to "테스트 캐릭터 설명",
                )

            `when`("등장인물을 추가하면") {
                val response = performAuthPost("/api/v1/novel-rooms/$novelRoomId/characters", request, account)

                then("소설에 등장인물이 추가된다.") {
                    response
                        .andExpect(status().isCreated)
                        .andExpect(jsonPath("$.id").exists())
                }
            }
        }

        given("특정 소설 공방 조회시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)
            val invalidUuid = "invalid-uuid-format"

            `when`("인증된 사용자가 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$novelRoomId", account)

                then("소설 공방이 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").exists())
                        .andExpect(jsonPath("$.title").exists())
                        .andExpect(jsonPath("$.description").exists())
                        .andExpect(jsonPath("$.synopsis").exists())
                        .andExpect(jsonPath("$.createdAt").exists())
                        .andExpect(jsonPath("$.role").exists())
                        .andExpect(jsonPath("$.contributorCount").exists())
                        .andExpect(jsonPath("$.maxContributorCount").exists())
                        .andExpect(jsonPath("$.status").exists())
                }
            }

            `when`("인증되지 않은 사용자가 조회하면") {
                val response = performGet("/api/v1/novel-rooms/$novelRoomId")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }

            `when`("잘못된 형식의 UUID로 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$invalidUuid", account)

                then("400 Bad Request 응답이 반환된다") {
                    response.andExpect(
                        jsonPath("$.code").value(ErrorCode.BAD_REQUEST.code),
                    )
                }
            }
        }

        given("소설 공방 등장인물 목록 조회시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)
            val invalidUuid = "invalid-uuid-format"

            `when`("등장인물 목록을 조회하면") {
                val response = performGet("/api/v1/novel-rooms/$novelRoomId/characters")

                then("등장인물 목록이 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$").isArray)
                }
            }

            `when`("잘못된 형식의 UUID로 조회하면") {
                val response = performGet("/api/v1/novel-rooms/$invalidUuid/characters")

                then("400 Bad Request 응답이 반환된다") {
                    response.andExpect(
                        jsonPath("$.code").value(ErrorCode.BAD_REQUEST.code),
                    )
                }
            }
        }

        given("소설 공방 참여자 목록 조회시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)
            val invalidUuid = "invalid-uuid-format"

            `when`("인증된 사용자가 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$novelRoomId/participants", account)

                then("참여자 목록이 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$").isArray)
                }
            }

            `when`("인증되지 않은 사용자가 조회하면") {
                val response = performGet("/api/v1/novel-rooms/$novelRoomId/participants")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }

            `when`("잘못된 형식의 UUID로 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$invalidUuid/participants", account)

                then("400 Bad Request 응답이 반환된다") {
                    response.andExpect(
                        jsonPath("$.code").value(ErrorCode.BAD_REQUEST.code),
                    )
                }
            }
        }

        given("소설 공방 참여자 순서 변경시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)
            val invalidUuid = "invalid-uuid-format"
            val validUuid = "f9c1a048-b0e4-464a-8a7f-59403ffa56e7"
            val request = mapOf("writingOrder" to 1)

            `when`("인증된 사용자가 순서를 변경하면") {
                val response =
                    performAuthPatch(
                        "/api/v1/novel-rooms/$novelRoomId/participants/${account.id}/order",
                        request,
                        account,
                    )

                then("참여자 순서가 변경된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").exists())
                }
            }

            `when`("인증되지 않은 사용자가 순서를 변경하면") {
                val response =
                    performPatch("/api/v1/novel-rooms/$novelRoomId/participants/${account.id}/order", request)

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }

            `when`("잘못된 형식의 novelRoomId UUID로 요청하면") {
                val response =
                    performAuthPatch(
                        "/api/v1/novel-rooms/$invalidUuid/participants/${account.id}/order",
                        request,
                        account,
                    )

                then("400 Bad Request 응답이 반환된다") {
                    response.andExpect(
                        jsonPath("$.code").value(ErrorCode.BAD_REQUEST.code),
                    )
                }
            }

            `when`("잘못된 형식의 participantId UUID로 요청하면") {
                val response =
                    performAuthPatch(
                        "/api/v1/novel-rooms/$novelRoomId/participants/$invalidUuid/order",
                        request,
                        account,
                    )

                then("400 Bad Request 응답이 반환된다") {
                    response.andExpect(
                        jsonPath("$.code").value(ErrorCode.BAD_REQUEST.code),
                    )
                }
            }
        }

        given("유효한 소설 공방 모집글 생성 요청이 주어지고") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)
            val request =
                mapOf(
                    "title" to "모집글 제목",
                    "content" to "모집글 내용",
                    "link" to "https://example.com/recruitment",
                )

            `when`("인증된 사용자가 소설 공방 모집글 생성을 요청하면") {
                val response = performAuthPost("/api/v1/novel-rooms/$novelRoomId/recruitments", request, account)

                then("모집글이 생성된다") {
                    response
                        .andExpect(status().isCreated)
                        .andExpect(jsonPath("$.id").exists())
                }
            }

            `when`("인증되지 않은 사용자가 소설 공방 모집글 생성을 요청하면") {
                val response = performPost("/api/v1/novel-rooms/$novelRoomId/recruitments", request)

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }

            `when`("소설 공방 소유자가 아닌 사용자가 모집글 생성을 요청하면") {
                val response = performAuthPost("/api/v1/novel-rooms/$novelRoomId/recruitments", request)

                then("권한 오류가 발생한다") {
                    response
                        .andExpect(status().isForbidden)
                        .andExpect(jsonPath("$.code").value(ErrorCode.NO_PERMISSION_TO_UPDATE.code))
                }
            }
        }

        given("유효하지 않은 소설 공방 모집글 생성 요청이 주어지고") {
            val novelRoomId = "test-novel-room-id"
            val invalidRequest =
                mapOf(
                    "title" to "",
                    "content" to "모집글 내용",
                    "link" to "",
                )

            `when`("인증된 사용자가 소설 공방 모집글 생성을 요청하면") {
                val response = performAuthPost("/api/v1/novel-rooms/$novelRoomId/recruitments", invalidRequest)

                then("유효성 검사 오류가 발생한다") {
                    response
                        .andExpect(status().isBadRequest)
                }
            }
        }

        given("소설 공방의 스토리 아크 목록을 조회할 수 있다") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)
            val invalidUuid = "invalid-uuid-format"

            `when`("인증된 사용자가 스토리 아크 목록을 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$novelRoomId/story-arcs", account)

                then("스토리 아크 목록이 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$").isArray)
                        .andExpect(jsonPath("$.length()").value(5))
                        .andExpect(jsonPath("$[0].phase").value("INTRODUCTION"))
                        .andExpect(jsonPath("$[1].phase").value("DEVELOPMENT"))
                        .andExpect(jsonPath("$[2].phase").value("CRISIS"))
                        .andExpect(jsonPath("$[3].phase").value("CLIMAX"))
                        .andExpect(jsonPath("$[4].phase").value("RESOLUTION"))
                }
            }

            `when`("인증되지 않은 사용자가 스토리 아크 목록을 조회하면") {
                val response = performGet("/api/v1/novel-rooms/$novelRoomId/story-arcs")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }

            `when`("권한이 없는 사용자가 스토리 아크 목록을 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$novelRoomId/story-arcs")

                then("권한 오류가 발생한다") {
                    response
                        .andExpect(status().isForbidden)
                        .andExpect(jsonPath("$.code").value(ErrorCode.NO_PERMISSION_TO_VIEW.code))
                }
            }

            `when`("잘못된 형식의 UUID로 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$invalidUuid/story-arcs", account)

                then("400 Bad Request 응답이 반환된다") {
                    response.andExpect(
                        jsonPath("$.code").value(ErrorCode.BAD_REQUEST.code),
                    )
                }
            }
        }

        given("소설 공방의 스토리 아크 목록 조회 시 소설 공방이 없으면 실패한다") {
            val account = fixtureAccount()
            val nonExistentNovelRoomId = "f9c1a048-b0e4-464a-8a7f-59403ffa56e7"

            `when`("인증된 사용자가 존재하지 않는 소설 공방의 스토리 아크 목록을 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$nonExistentNovelRoomId/story-arcs", account)

                then("소설 공방이 없다는 오류가 발생한다") {
                    response
                        .andExpect(status().isNotFound)
                        .andExpect(jsonPath("$.code").value(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND.code))
                }
            }
        }

        given("스토리 아크의 스토리 페이즈를 수정할 때") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)
            val phase = StoryPhaseEnum.DEVELOPMENT

            val request =
                mapOf(
                    "description" to "Test description",
                    "startChapterNumber" to 0,
                    "endChapterNumber" to 1,
                )

            `when`("대표 작가가 스토리 페이즈를 수정하면") {
                val response = performAuthPatch("/api/v1/novel-rooms/$novelRoomId/story-arcs/$phase", request, account)

                then("스토리 페이즈가 수정된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").exists())
                }
            }

            `when`("인증되지 않은 사용자가 스토리 페이즈를 수정하면") {
                val response = performPatch("/api/v1/novel-rooms/$novelRoomId/story-arcs/$phase", request)

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }

        given("챕터 텍스트 조회 시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            val chapterId = fixtureChapter(novelRoomId, account)

            `when`("인증된 사용자가 챕터 텍스트를 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$novelRoomId/chapters/$chapterId/texts", account)

                then("챕터 텍스트 목록이 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.items").isArray)
                }
            }

            `when`("인증되지 않은 사용자가 챕터 텍스트를 조회하면") {
                val response = performGet("/api/v1/novel-rooms/$novelRoomId/chapters/$chapterId/texts")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }

            `when`("존재하지 않는 소설 공방의 챕터 텍스트를 조회하면") {
                val invalidNovelRoomId = "00000000-0000-0000-0000-000000000000"
                val response =
                    performAuthGet("/api/v1/novel-rooms/$invalidNovelRoomId/chapters/$chapterId/texts", account)

                then("소설 공방을 찾을 수 없다는 오류가 발생한다") {
                    response
                        .andExpect(status().isNotFound)
                }
            }
        }

        given("등장인물 일괄 업데이트 시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)
            val request =
                mapOf(
                    "characters" to
                            listOf(
                                mapOf(
                                    "name" to "주인공",
                                    "description" to "주인공 설명",
                                ),
                                mapOf(
                                    "name" to "조연",
                                    "description" to "조연 설명",
                                ),
                            ),
                )

            `when`("인증된 사용자가 등장인물을 일괄 업데이트하면") {
                val response = performAuthPut("/api/v1/novel-rooms/$novelRoomId/characters", request, account)

                then("등장인물이 업데이트된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.characters").isArray)
                        .andExpect(jsonPath("$.characters.length()").value(2))
                }
            }

            `when`("인증되지 않은 사용자가 등장인물을 일괄 업데이트하면") {
                val response = performPut("/api/v1/novel-rooms/$novelRoomId/characters", request)

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }

        given("챕터 업데이트 시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            val chapterId = fixtureChapter(novelRoomId, account)

            val request =
                mapOf(
                    "title" to "업데이트된 챕터 제목",
                )

            `when`("인증된 사용자가 챕터를 업데이트하면") {
                val response =
                    performAuthPatch("/api/v1/novel-rooms/$novelRoomId/chapters/$chapterId", request, account)

                then("챕터가 업데이트된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").exists())
                }
            }

            `when`("인증되지 않은 사용자가 챕터를 업데이트하면") {
                val response = performPatch("/api/v1/novel-rooms/$novelRoomId/chapters/$chapterId", request)

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }

        given("드래프트 챕터 텍스트 조회 시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            val chapterId = fixtureChapter(novelRoomId, account)

            `when`("인증된 사용자가 드래프트 챕터 텍스트를 조회하면") {
                val response =
                    performAuthGet("/api/v1/novel-rooms/$novelRoomId/chapters/$chapterId/draft-text", account)

                then("드래프트 챕터 텍스트가 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.content").exists())
                }
            }

            `when`("인증되지 않은 사용자가 드래프트 챕터 텍스트를 조회하면") {
                val response = performGet("/api/v1/novel-rooms/$novelRoomId/chapters/$chapterId/draft-text")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }

        given("드래프트 챕터 텍스트 업데이트 시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            val chapterId = fixtureChapter(novelRoomId, account)

            val request =
                mapOf(
                    "content" to "업데이트된 챕터 내용입니다.",
                )

            `when`("인증된 사용자가 드래프트 챕터 텍스트를 업데이트하면") {
                val response = performAuthPatch(
                    "/api/v1/novel-rooms/$novelRoomId/chapters/$chapterId/draft-text",
                    request,
                    account,
                )

                then("드래프트 챕터 텍스트가 업데이트된다") {
                    response
                        .andExpect(status().isNoContent)
                }
            }

            `when`("인증되지 않은 사용자가 드래프트 챕터 텍스트를 업데이트하면") {
                val response = performPatch("/api/v1/novel-rooms/$novelRoomId/chapters/$chapterId/draft-text", request)

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }

        given("챕터 텍스트 확정 시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            val chapterId = fixtureChapter(novelRoomId, account)

            `when`("인증된 사용자가 챕터 텍스트를 확정하면") {
                val response = performAuthPost(
                    "/api/v1/novel-rooms/$novelRoomId/chapters/$chapterId/finalize",
                    emptyMap<String, String>(),
                    account,
                )

                then("챕터 텍스트가 확정된다") {
                    response
                        .andExpect(status().isOk)
                }
            }

            `when`("인증되지 않은 사용자가 챕터 텍스트를 확정하면") {
                val response = performPost("/api/v1/novel-rooms/$novelRoomId/chapters/$chapterId/finalize")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }

        given("참여 요청 승인 시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)
            val requesterAccount = fixtureAccount()
            val requesterId = requesterAccount.id

            `when`("인증된 사용자가 참여 요청을 승인하면") {
                val response =
                    performAuthPost("/api/v1/novel-rooms/$novelRoomId/join-requests/$requesterId/approve", account)

                then("참여 요청이 승인된다") {
                    response
                        .andExpect(status().isNoContent)
                }
            }

            `when`("인증되지 않은 사용자가 참여 요청을 승인하면") {
                val response = performPost("/api/v1/novel-rooms/$novelRoomId/join-requests/$requesterId/approve")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }

        given("참여 요청 거절 시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)
            val requesterAccount = fixtureAccount()
            val requesterId = requesterAccount.id

            `when`("인증된 사용자가 참여 요청을 거절하면") {
                val response =
                    performAuthPost("/api/v1/novel-rooms/$novelRoomId/join-requests/$requesterId/reject", account)

                then("참여 요청이 거절된다") {
                    response
                        .andExpect(status().isNoContent)
                }
            }

            `when`("인증되지 않은 사용자가 참여 요청을 거절하면") {
                val response = performPost("/api/v1/novel-rooms/$novelRoomId/join-requests/$requesterId/reject")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }
    }
}
