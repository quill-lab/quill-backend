package lab.ujumeonji.literaturebackend.usecase.auth

import lab.ujumeonji.literaturebackend.domain.account.AccountService
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Component
@Transactional(readOnly = true)
class CheckEmailDuplicationUseCase(
    private val accountRepository: AccountService,
) : UseCase<CheckEmailDuplicationUseCase.Request, CheckEmailDuplicationUseCase.Response> {
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val account = accountRepository.findByEmail(request.email)
        return Response(isDuplicated = Objects.isNull(account))
    }

    data class Request(
        val email: String,
    )

    data class Response(
        val isDuplicated: Boolean,
    )
}
