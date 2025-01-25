package lab.ujumeonji.literaturebackend.support

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCase
import io.kotest.extensions.spring.SpringExtension
import io.mockk.mockk
import lab.ujumeonji.literaturebackend.support.mail.MailPort
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
abstract class ControllerTest(
    private val mockMvc: MockMvc,
    val objectMapper: ObjectMapper,
) : BehaviorSpec() {

    protected val mailPort: MailPort = mockk<MailPort>()

    override fun extensions() = listOf(SpringExtension)

    override suspend fun beforeEach(testCase: TestCase) = Unit

    protected fun toJson(obj: Any): String {
        return objectMapper.writeValueAsString(obj)
    }

    protected fun fromJson(json: String, clazz: Class<*>): Any {
        return objectMapper.readValue(json, clazz)
    }

    protected fun performGet(url: String) = mockMvc.perform(
        MockMvcRequestBuilders.get(url)
            .accept(MediaType.APPLICATION_JSON)
    ).andDo(MockMvcResultHandlers.print())

    fun performPost(url: String, body: Any) = mockMvc.perform(
        MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(body))
    ).andDo(MockMvcResultHandlers.print())

    protected fun performPut(url: String, body: Any) = mockMvc.perform(
        MockMvcRequestBuilders.put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(toJson(body))
    ).andDo(MockMvcResultHandlers.print())

    protected fun performDelete(url: String) = mockMvc.perform(
        MockMvcRequestBuilders.delete(url)
            .accept(MediaType.APPLICATION_JSON)
    ).andDo(MockMvcResultHandlers.print())
}
