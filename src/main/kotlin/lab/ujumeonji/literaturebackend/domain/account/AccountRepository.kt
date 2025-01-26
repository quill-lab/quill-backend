package lab.ujumeonji.literaturebackend.domain.account

import org.springframework.data.repository.CrudRepository
import java.util.*

interface AccountRepository : CrudRepository<Account, UUID> {
    fun findByEmail(email: String): Account?
}
