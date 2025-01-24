package lab.ujumeonji.literaturebackend.api.novel

import com.fasterxml.jackson.databind.ObjectMapper
import lab.ujumeonji.literaturebackend.support.AuthControllerTest
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class NovelRoomApiControllerTest(
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
    }
}
