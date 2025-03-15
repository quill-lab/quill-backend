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

            `when`("인증되지 않은 사용자가 수정을 요청하면") {
                val request = mapOf(
                    "title" to "수정된 제목",
                    "description" to "수정된 설명",
                    "category" to "FANTASY",
                    "tags" to listOf("판타지"),
                    "synopsis" to "수정된 시놉시스"
                )
                val response = performPatch("/api/v1/novel-rooms/$novelRoomId", request)

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }

            `when`("소설 공방 소유자가 아닌 사용자가 수정을 요청하면") {
                val request = mapOf(
                    "title" to "수정된 제목",
                    "description" to "수정된 설명", 
                    "category" to "FANTASY",
                    "tags" to listOf("판타지"),
                    "synopsis" to "수정된 시놉시스"
                )
                val response = performAuthPatch("/api/v1/novel-rooms/$novelRoomId", request)

                then("권한 오류가 발생한다") {
                    response
                        .andExpect(status().isForbidden)
                        .andExpect(jsonPath("$.code").value(ErrorCode.NO_PERMISSION_TO_UPDATE.code))
                }
            }
        }
    }
}
