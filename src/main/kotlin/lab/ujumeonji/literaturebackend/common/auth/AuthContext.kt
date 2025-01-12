package lab.ujumeonji.literaturebackend.common.auth

import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope
class AuthContext {
    var userId: Long? = null
}
