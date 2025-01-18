package lab.ujumeonji.literaturebackend.domain.contributor

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface ContributorRepository : CrudRepository<Contributor, Long> {

    @Query(
        """
        SELECT c FROM Contributor c
        WHERE c.accountId = :accountId
        AND c.role = :#{#role}
        AND c.deletedAt IS NULL
        ORDER BY c.createdAt DESC
    """
    )
    fun findAllByAccountIdAndRole(
        @Param("accountId") accountId: Long,
        @Param("role") role: ContributorRole = ContributorRole.MAIN
    ): List<Contributor>
}
