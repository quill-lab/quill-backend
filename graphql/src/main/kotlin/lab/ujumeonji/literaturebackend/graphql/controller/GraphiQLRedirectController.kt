package lab.ujumeonji.literaturebackend.graphql.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class GraphiQLRedirectController {

    @GetMapping("/graphiql")
    fun redirect(): String {
        return "forward:/graphiql/index.html"
    }
}
