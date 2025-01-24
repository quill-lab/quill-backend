package lab.ujumeonji.literaturebackend.api.novel

import com.fasterxml.jackson.databind.ObjectMapper
import lab.ujumeonji.literaturebackend.support.ControllerTest
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class NovelApiControllerTest(
    mockMvc: MockMvc,
    objectMapper: ObjectMapper,
) : ControllerTest(
    mockMvc = mockMvc,
    objectMapper = objectMapper,
) {

    init {
        given("소설 카테고리 목록이 존재하고") {
            `when`("소설 카테고리 목록 조회를 요청하면") {
                val response = performGet("/api/v1/novels/categories")

                then("모든 카테고리 목록이 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$").isArray)
                        .andExpect(jsonPath("$[*].name").exists())
                        .andExpect(jsonPath("$[*].alias").exists())
                }
            }
        }

        given("존재하지 않는 소설의 ID가 주어지고") {
            val nonExistentNovelId = 999L

            `when`("해당 소설의 등장인물 목록을 조회하면") {
                val response = performGet("/api/v1/novels/$nonExistentNovelId/characters")

                then("소설을 찾을 수 없다는 에러가 반환된다") {
                    response
                        .andExpect(status().isNotFound)
                        .andExpect(jsonPath("$.code").value(ErrorCode.NOVEL_NOT_FOUND.code))
                }
            }
        }
    }
}
