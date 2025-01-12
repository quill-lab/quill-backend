package lab.ujumeonji.literaturebackend.domain.account

import lab.ujumeonji.literaturebackend.domain.account.command.CreateAccountCommand
import lab.ujumeonji.literaturebackend.support.encrypt.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class AccountService(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun findOneByEmail(email: String): Account? = accountRepository.findByEmail(email)

    fun findByIds(ids: List<Long>): List<Account> = accountRepository.findAllById(ids).toList()

    fun checkPassword(account: Account, password: String): Boolean = account.checkPassword(
        password,
        passwordEncoder
    )

    fun create(command: CreateAccountCommand, now: LocalDateTime): Account {
        val account = Account.create(
            command.email,
            command.nickname,
            passwordEncoder.encode(command.password),
            now
        )

        return accountRepository.save(account)
    }
}
