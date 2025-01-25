package lab.ujumeonji.literaturebackend.usecase.auth

import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.domain.account.command.CreateAccountCommand
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class SignUpUseCase(
    private val accountService: AccountService,
) : UseCase<SignUpUseCase.Request, SignUpUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        accountService.findOneByEmail(request.email)?.let {
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

        return Response(account.id)
    }

    data class Request(
        val email: String,
        val password: String,
        val nickname: String
    )

    data class Response(
        val id: Long,
    )
}
