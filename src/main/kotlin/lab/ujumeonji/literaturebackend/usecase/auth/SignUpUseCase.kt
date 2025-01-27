package lab.ujumeonji.literaturebackend.usecase.auth

import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.account.command.CreateAccountCommand
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.support.mail.MailPort
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.concurrent.CompletableFuture

@Component
@Transactional
class SignUpUseCase(
    private val accountService: AccountService,
    private val mailPort: MailPort,
) : UseCase<SignUpUseCase.Request, SignUpUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        accountService.findByEmail(request.email)?.let {
            throw BusinessException(ErrorCode.DUPLICATE_EMAIL)
        }

        val account = accountService.create(
            command = CreateAccountCommand(
                email = request.email,
                password = request.password,
                nickname = request.nickname
            ),
            now = executedAt
        )

        sendWelcomeEmail(request.email)

        return Response(id = account.id.id.toString())
    }

    private fun sendWelcomeEmail(email: String) {
        val subject = "Welcome to Literature Backend!"
        val htmlContent = """
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <h2>Welcome to Literature Backend!</h2>
                <p>We're thrilled to have you on board!</p>
                <p>
                    Please check your mailbox for the verification email.
                    If you don't see it, please check your spam folder or try resending the verification email.
                </p>
                <hr>
                <p style="color: #666; font-size: 12px;">This email is sent automatically and does not accept replies.</p>
            </div>
        """.trimIndent()

        CompletableFuture.runAsync {
            try {
                mailPort.sendHtmlEmail(email, subject, htmlContent)
            } catch (e: Exception) {
                logger.error("Failed to send welcome email. email=$email", e)
            }
        }
    }

    data class Request(
        val email: String,
        val password: String,
        val nickname: String
    )

    data class Response(
        val id: String,
    )

    companion object {
        private val logger: Logger = LogManager.getLogger(SignUpUseCase::class.java)
    }
}
