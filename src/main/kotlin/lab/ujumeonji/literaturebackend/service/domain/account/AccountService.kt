package lab.ujumeonji.literaturebackend.service.domain.account

import lab.ujumeonji.literaturebackend.domain.account.Account
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AccountService(
    private val accountRepository: AccountRepository
) {

    fun findOneByEmail(email: String): Account? = accountRepository.findByEmail(email)

    fun findByIds(ids: List<Long>): List<Account> = accountRepository.findAllById(ids).toList()
}
