package lab.ujumeonji.literaturebackend.api.novel

import com.fasterxml.jackson.databind.ObjectMapper
import lab.ujumeonji.literaturebackend.support.AuthControllerTest
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class NovelRoomApiControllerTest @Autowired constructor(
    mockMvc: MockMvc,
    objectMapper: ObjectMapper,
) : AuthControllerTest(
    mockMvc = mockMvc,
    objectMapper = objectMapper,
) {

    init {
        given("유효한 소설 공방 생성 요청이 주어지고") {
            val request = mapOf(
                "maxContributors" to 5,
                "title" to "테스트 소설",
                "description" to "테스트 소설 설명",
                "category" to "GENERAL",
                "tags" to listOf("태그1", "태그2"),
                "synopsis" to "테스트 소설의 줄거리입니다.",
                "coverImage" to "https://example.com/cover.jpg"
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

            val request = mapOf(
                "name" to "테스트 캐릭터",
                "description" to "테스트 캐릭터 설명"
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

        given("소설 공방 목록 조회시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            `when`("인증된 사용자가 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms?page=0&size=10", account)

                then("소설 공방 목록이 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.items").isArray)
                        .andExpect(jsonPath("$.totalCount").exists())
                        .andExpect(jsonPath("$.size").value(10))
                        .andExpect(jsonPath("$.page").value(0))
                }
            }

            `when`("인증되지 않은 사용자가 조회하면") {
                val response = performGet("/api/v1/novel-rooms?page=0&size=10")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }

        given("특정 소설 공방 조회시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            `when`("인증된 사용자가 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$novelRoomId", account)

                then("소설 공방 정보가 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").value(novelRoomId))
                        .andExpect(jsonPath("$.title").exists())
                        .andExpect(jsonPath("$.category").exists())
                }
            }

            `when`("존재하지 않는 소설 공방을 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/999999", account)

                then("Not Found 오류가 발생한다") {
                    response
                        .andExpect(status().isNotFound)
                }
            }
        }

        given("소설 공방 수정시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            val request = mapOf(
                "title" to "수정된 제목",
                "description" to "수정된 설명",
                "category" to "GENERAL",
                "tags" to listOf("수정태그1", "수정태그2"),
                "synopsis" to "수정된 줄거리입니다."
            )

            `when`("인증된 사용자가 수정하면") {
                val response = performAuthPatch("/api/v1/novel-rooms/$novelRoomId", request, account)

                then("소설 공방이 수정된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").exists())
                }
            }
        }

        given("소설 등장인물 목록 조회시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            `when`("등장인물 목록을 조회하면") {
                val response = performGet("/api/v1/novel-rooms/$novelRoomId/characters")

                then("등장인물 목록이 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$").isArray)
                }
            }
        }

        given("소설 공방 참여자 목록 조회시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            `when`("참여자가 목록을 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$novelRoomId/participants", account)

                then("참여자 목록이 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$").isArray)
                        .andExpect(jsonPath("$[0].id").exists())
                        .andExpect(jsonPath("$[0].nickname").exists())
                        .andExpect(jsonPath("$[0].role").exists())
                        .andExpect(jsonPath("$[0].writingOrder").exists())
                        .andExpect(jsonPath("$[0].joinedAt").exists())
                }
            }

            `when`("참여하지 않은 사용자가 목록을 조회하면") {
                val otherAccount = fixtureAccount()
                val response = performAuthGet("/api/v1/novel-rooms/$novelRoomId/participants", otherAccount)

                then("조회 권한 없음 오류가 발생한다") {
                    response
                        .andExpect(status().isForbidden)
                        .andExpect(jsonPath("$.code").value(ErrorCode.NO_PERMISSION_TO_VIEW.code))
                }
            }

            `when`("인증되지 않은 사용자가 목록을 조회하면") {
                val response = performGet("/api/v1/novel-rooms/$novelRoomId/participants")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }

        given("소설 공방 참여자 순서 변경시") {
            val mainAccount = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(mainAccount)

            val subAccount = fixtureAccount()
            performAuthPost(
                "/api/v1/novel-rooms/$novelRoomId/participants",
                mapOf("accountId" to subAccount.id), mainAccount
            )

            `when`("소설 공방 대표 작가가 참여자 순서를 변경하면") {
                val request = mapOf(
                    "writingOrder" to 1
                )
                val response = performAuthPatch(
                    "/api/v1/novel-rooms/$novelRoomId/participants/${subAccount.id}/order",
                    request,
                    mainAccount
                )

                then("참여자 순서가 변경된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").exists())
                }
            }

            `when`("일반 참여자가 다른 참여자의 순서를 변경하면") {
                val request = mapOf(
                    "writingOrder" to 0
                )
                val response = performAuthPatch(
                    "/api/v1/novel-rooms/$novelRoomId/participants/${mainAccount.id}/order",
                    request,
                    subAccount
                )

                then("수정 권한 없음 오류가 발생한다") {
                    response
                        .andExpect(status().isForbidden)
                        .andExpect(jsonPath("$.code").value(ErrorCode.NO_PERMISSION_TO_UPDATE.code))
                }
            }

            `when`("일반 참여자가 자신의 순서를 변경하려고 하면") {
                val request = mapOf(
                    "writingOrder" to 0
                )
                val response = performAuthPatch(
                    "/api/v1/novel-rooms/$novelRoomId/participants/${subAccount.id}/order",
                    request,
                    subAccount
                )

                then("수정 권한 없음 오류가 발생한다") {
                    response
                        .andExpect(status().isForbidden)
                        .andExpect(jsonPath("$.code").value(ErrorCode.NO_PERMISSION_TO_UPDATE.code))
                }
            }

            `when`("참여하지 않은 사용자가 순서를 변경하면") {
                val otherAccount = fixtureAccount()
                val request = mapOf(
                    "writingOrder" to 1
                )
                val response = performAuthPatch(
                    "/api/v1/novel-rooms/$novelRoomId/participants/${subAccount.id}/order",
                    request,
                    otherAccount
                )

                then("수정 권한 없음 오류가 발생한다") {
                    response
                        .andExpect(status().isForbidden)
                        .andExpect(jsonPath("$.code").value(ErrorCode.NO_PERMISSION_TO_UPDATE.code))
                }
            }

            `when`("인증되지 않은 사용자가 순서를 변경하면") {
                val request = mapOf(
                    "writingOrder" to 1
                )
                val response =
                    performPatch("/api/v1/novel-rooms/$novelRoomId/participants/${subAccount.id}/order", request)

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }

            `when`("존재하지 않는 참여자의 순서를 변경하면") {
                val request = mapOf(
                    "writingOrder" to 1
                )
                val response =
                    performAuthPatch(
                        "/api/v1/novel-rooms/$novelRoomId/participants/0194a1e8-6aad-7c73-9de0-b216346a242f/order",
                        request,
                        mainAccount
                    )

                then("참여자를 찾을 수 없다는 오류가 발생한다") {
                    response
                        .andExpect(status().isNotFound)
                        .andExpect(jsonPath("$.code").value(ErrorCode.CONTRIBUTOR_NOT_FOUND.code))
                }
            }
        }
    }
}
