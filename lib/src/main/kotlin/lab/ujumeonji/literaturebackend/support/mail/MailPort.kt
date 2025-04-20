package lab.ujumeonji.literaturebackend.support.mail

interface MailPort {
    fun sendHtmlEmail(
        to: String,
        subject: String,
        htmlContent: String,
    )
}
