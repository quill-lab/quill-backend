package lab.ujumeonji.literaturebackend.support.mail.impl

import lab.ujumeonji.literaturebackend.support.mail.MailPort
import org.springframework.stereotype.Component

@Component
class DebugMailAdapter : MailPort {
    override fun sendHtmlEmail(to: String, subject: String, htmlContent: String) = Unit
}
