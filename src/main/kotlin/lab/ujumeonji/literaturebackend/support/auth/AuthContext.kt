package lab.ujumeonji.literaturebackend.support.auth

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
class AuthContext {
    var userId: Long? = null
}
