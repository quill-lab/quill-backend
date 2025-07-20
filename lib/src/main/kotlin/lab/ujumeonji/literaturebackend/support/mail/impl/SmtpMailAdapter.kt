package lab.ujumeonji.literaturebackend.support.mail.impl

import lab.ujumeonji.literaturebackend.support.mail.MailPort
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component

@Component
class SmtpMailAdapter(
    private val mailSender: JavaMailSender,
) : MailPort {
    override fun sendHtmlEmail(
        to: String,
        subject: String,
        htmlContent: String,
    ) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(htmlContent, true)

        mailSender.send(message)
    }
}
