package lab.ujumeonji.literaturebackend.domain.contributor

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface ContributorGroupRepository : CrudRepository<ContributorGroup, Long> {

    @Query(
        """
        SELECT DISTINCT cg FROM ContributorGroup cg
        JOIN FETCH cg.contributors c
        WHERE c.accountId = :accountId
        AND c.deletedAt IS NULL
        AND cg.deletedAt IS NULL
        ORDER BY cg.createdAt DESC
        """
    )
    fun findByAccountId(
        @Param("accountId") accountId: Long,
        pageable: Pageable
    ): Page<ContributorGroup>
}
