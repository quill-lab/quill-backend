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

    fun findByEmail(email: String): Account? = accountRepository.findByEmail(email)

    fun findByName(email: String): Account? = accountRepository.findByName(email)

    fun findById(id: AccountId): Account? = accountRepository.findById(id.id).orElse(null)

    fun findByIds(ids: List<AccountId>): List<Account> = accountRepository.findAllById(ids.map { it.id }).toList()

    fun checkPassword(id: AccountId, password: String): Boolean {
        val account = findById(id) ?: return false
        return account.checkPassword(password, passwordEncoder)
    }

    @Transactional
    fun updatePassword(id: AccountId, password: String) {
        val account = findById(id) ?: return
        account.updatePassword(password, passwordEncoder)
        accountRepository.save(account)
    }

    @Transactional
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
