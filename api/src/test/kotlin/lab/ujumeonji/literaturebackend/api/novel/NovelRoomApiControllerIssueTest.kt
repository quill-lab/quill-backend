package lab.ujumeonji.literaturebackend.api.novel

import com.fasterxml.jackson.databind.ObjectMapper
import lab.ujumeonji.literaturebackend.support.AuthControllerTest
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class NovelRoomApiControllerIssueTest @Autowired constructor(
    mockMvc: MockMvc,
    objectMapper: ObjectMapper,
) : AuthControllerTest(
    mockMvc = mockMvc,
    objectMapper = objectMapper,
) {

    init {
        given("소설 공방 수정 요청이 주어지고") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            `when`("제목, 설명, 카테고리, 태그, 시놉시스를 수정하면") {
                val request = mapOf(
                    "title" to "수정된 제목",
                    "description" to "수정된 설명",
                    "category" to "FANTASY",
                    "tags" to listOf("판타지", "로맨스"),
                    "synopsis" to "수정된 시놉시스"
                )
                val response = performAuthPatch("/api/v1/novel-rooms/$novelRoomId", request, account)

                then("소설 공방이 성공적으로 수정된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.id").exists())
                }
            }
        }

        given("소설 공방의 스토리 아크 목록 조회 시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            `when`("스토리 아크 목록을 조회하면") {
                val response = performAuthGet("/api/v1/novel-rooms/$novelRoomId/story-arcs", account)

                then("스토리 아크가 전개 순서대로 조회된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$[0].phase").value("INTRODUCTION"))
                        .andExpect(jsonPath("$[1].phase").value("DEVELOPMENT"))
                        .andExpect(jsonPath("$[2].phase").value("CRISIS"))
                        .andExpect(jsonPath("$[3].phase").value("CLIMAX"))
                        .andExpect(jsonPath("$[4].phase").value("RESOLUTION"))
                }
            }
        }
    }
}
