package lab.ujumeonji.literaturebackend.domain.contributor

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface ContributorRepository : CrudRepository<Contributor, UUID> {

    @Query(
        """
        SELECT c FROM Contributor c
        WHERE c.accountId = :accountId
        AND c.role = :#{#role}
        ORDER BY c.createdAt DESC
    """
    )
    fun findAllByAccountIdAndRole(
        @Param("accountId") accountId: UUID,
        @Param("role") role: ContributorRole = ContributorRole.MAIN
    ): List<Contributor>
}
