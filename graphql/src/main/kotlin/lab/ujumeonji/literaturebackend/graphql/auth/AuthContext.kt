package lab.ujumeonji.literaturebackend.graphql.auth

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
class AuthContext {
    var accountId: String? = null
}
