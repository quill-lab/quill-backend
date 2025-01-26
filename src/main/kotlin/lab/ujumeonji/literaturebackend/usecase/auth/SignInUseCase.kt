package lab.ujumeonji.literaturebackend.usecase.auth

import lab.ujumeonji.literaturebackend.domain.account.Account
import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.support.session.TokenManager
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class SignInUseCase(
    private val accountService: AccountService,
    private val tokenManager: TokenManager,
) : UseCase<SignInUseCase.Request, SignInUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        val account = findAccount(request.email)
        validatePassword(account, request.password)
        val token = createAuthToken(account, executedAt)
        return Response(token)
    }

    private fun findAccount(email: String) =
        accountService.findOneByEmail(email)
            ?: throw BusinessException(ErrorCode.INVALID_CREDENTIALS)

    private fun validatePassword(account: Account, password: String) {
        if (!accountService.checkPassword(account, password)) {
            throw BusinessException(ErrorCode.INVALID_CREDENTIALS)
        }
    }

    private fun createAuthToken(account: Account, now: LocalDateTime): String =
        tokenManager.createToken(
            payload = mapOf(
                "id" to account.id.toString(),
                "email" to account.email,
            ),
            issuedAt = now
        )

    data class Request(
        val email: String,
        val password: String
    )

    data class Response(
        val token: String
    )
}
