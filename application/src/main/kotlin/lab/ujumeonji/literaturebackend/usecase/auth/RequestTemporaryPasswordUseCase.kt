package lab.ujumeonji.literaturebackend.usecase.auth

import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.support.mail.MailPort
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Component
@Transactional
class RequestTemporaryPasswordUseCase(
    private val accountService: AccountService,
    private val mailPort: MailPort,
) : UseCase<RequestTemporaryPasswordUseCase.Request, RequestTemporaryPasswordUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val account = accountService.findByEmail(request.email)
            ?: throw BusinessException(ErrorCode.EMAIL_NOT_FOUND)

        val temporaryPassword = generateTemporaryPassword()
        accountService.updatePassword(account.idValue, temporaryPassword)

        mailPort.sendHtmlEmail(
            to = request.email,
            subject = "",
            htmlContent = "",
        )

        return Response
    }

    private fun generateTemporaryPassword(): String = UUID.randomUUID().toString().take(8)

    data class Request(
        val email: String,
    )

    object Response
}
