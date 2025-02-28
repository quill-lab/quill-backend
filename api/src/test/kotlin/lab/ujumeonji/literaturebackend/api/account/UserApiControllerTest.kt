package lab.ujumeonji.literaturebackend.api.account

import com.fasterxml.jackson.databind.ObjectMapper
import lab.ujumeonji.literaturebackend.support.AuthControllerTest
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserApiControllerTest @Autowired constructor(
    mockMvc: MockMvc,
    objectMapper: ObjectMapper,
) : AuthControllerTest(
    mockMvc = mockMvc,
    objectMapper = objectMapper,
) {

    init {
        given("참여한 소설 공방 목록 조회시") {
            val account = fixtureAccount()
            val novelRoomId = fixtureNovelRoom(account)

            `when`("인증된 사용자가 최신순으로 조회하면") {
                val response = performAuthGet("/api/v1/users/me/novel-rooms?page=0&size=10&sort=LATEST", account)

                then("소설 공방 목록이 최신순으로 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.items").isArray)
                        .andExpect(jsonPath("$.totalCount").exists())
                        .andExpect(jsonPath("$.size").value(10))
                        .andExpect(jsonPath("$.page").value(0))
                }
            }

            `when`("인증된 사용자가 오래된순으로 조회하면") {
                val response = performAuthGet("/api/v1/users/me/novel-rooms?page=0&size=10&sort=OLDEST", account)

                then("소설 공방 목록이 오래된순으로 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.items").isArray)
                        .andExpect(jsonPath("$.totalCount").exists())
                        .andExpect(jsonPath("$.size").value(10))
                        .andExpect(jsonPath("$.page").value(0))
                }
            }

            `when`("인증된 사용자가 잘못된 정렬 방식으로 조회하면") {
                val response = performAuthGet("/api/v1/users/me/novel-rooms?page=0&size=10&sort=INVALID", account)

                then("Bad Request 오류가 발생한다") {
                    response
                        .andExpect(status().isBadRequest)
                }
            }

            `when`("인증되지 않은 사용자가 조회하면") {
                val response = performGet("/api/v1/users/me/novel-rooms?page=0&size=10")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }

        given("소설 공방 참여 신청 목록 조회시") {
            val account = fixtureAccount()

            `when`("인증된 사용자가 최신순으로 조회하면") {
                val response = performAuthGet("/api/v1/users/me/contributor-requests?page=0&size=10", account)

                then("참여 신청 목록이 최신순으로 반환된다") {
                    response
                        .andExpect(status().isOk)
                        .andExpect(jsonPath("$.items").isArray)
                        .andExpect(jsonPath("$.totalCount").exists())
                        .andExpect(jsonPath("$.size").value(10))
                        .andExpect(jsonPath("$.page").value(0))
                }
            }

            `when`("인증되지 않은 사용자가 조회하면") {
                val response = performGet("/api/v1/users/me/contributor-requests?page=0&size=10")

                then("인증 오류가 발생한다") {
                    response
                        .andExpect(status().isUnauthorized)
                        .andExpect(jsonPath("$.code").value(ErrorCode.UNAUTHORIZED.code))
                }
            }
        }
    }
}
