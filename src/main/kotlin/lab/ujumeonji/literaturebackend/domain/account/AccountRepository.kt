package lab.ujumeonji.literaturebackend.domain.account

import lab.ujumeonji.literaturebackend.domain.account.Account
import org.springframework.data.repository.CrudRepository

interface AccountRepository : CrudRepository<Account, Long> {
    fun findByEmail(email: String): Account?
}
