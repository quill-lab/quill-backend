package lab.ujumeonji.literaturebackend.usecase.auth

import lab.ujumeonji.literaturebackend.service.domain.account.AccountService
import lab.ujumeonji.literaturebackend.service.encrypt.PasswordEncoder
import lab.ujumeonji.literaturebackend.service.session.TokenManager
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class SignInUseCase(
    private val passwordEncoder: PasswordEncoder,
    private val accountService: AccountService,
    private val tokenManager: TokenManager,
) : UseCase<SignInUseCase.Request, SignInUseCase.Response> {

    override fun execute(request: Request): Response {
        val now = LocalDateTime.now()

        val account =
            accountService.findOneByEmail(request.email) ?: throw IllegalArgumentException("Account not found")

        if (!account.checkPassword(
                request.password,
                passwordEncoder,
            )
        ) {
            throw IllegalArgumentException("Invalid password")
        }

        val token = tokenManager.createToken(
            payload = mapOf(
                "id" to account.id,
                "email" to account.email,
            ),
            issuedAt = now
        )

        return Response(token)
    }

    data class Request(
        val email: String, val password: String
    )

    data class Response(
        val token: String
    )
}
