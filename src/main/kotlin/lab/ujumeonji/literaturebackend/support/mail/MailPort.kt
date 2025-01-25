package lab.ujumeonji.literaturebackend.support.mail

interface MailPort {

    suspend fun sendHtmlEmail(to: String, subject: String, htmlContent: String)
}
