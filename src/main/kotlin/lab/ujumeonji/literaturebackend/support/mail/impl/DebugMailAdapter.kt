package lab.ujumeonji.literaturebackend.support.mail.impl

import lab.ujumeonji.literaturebackend.support.mail.MailPort
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Component

@Component
class DebugMailAdapter : MailPort {
    override suspend fun sendHtmlEmail(to: String, subject: String, htmlContent: String) {
        logger.info("Sending email:")
        logger.info("To: $to")
        logger.info("Subject: $subject")
        logger.info("Content: $htmlContent")
    }

    companion object {
        private val logger: Logger = LogManager.getLogger(DebugMailAdapter::class.java)
    }
}
