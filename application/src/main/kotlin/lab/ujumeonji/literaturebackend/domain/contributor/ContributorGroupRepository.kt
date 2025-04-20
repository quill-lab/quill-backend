package lab.ujumeonji.literaturebackend.domain.contributor

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import java.util.*

interface ContributorGroupRepository : CrudRepository<ContributorGroup, UUID> {
    @Query(
        """
        SELECT DISTINCT cg FROM ContributorGroup cg
        JOIN FETCH cg.contributors c
        WHERE c.accountId = :accountId
        AND c.deletedAt IS NULL
        AND cg.deletedAt IS NULL
        """,
    )
    fun findByAccountId(
        @Param("accountId") accountId: UUID,
        pageable: Pageable,
    ): Page<ContributorGroup>

    @Query(
        """
        SELECT cg FROM ContributorGroup cg
        WHERE cg.novelId = :novelId
        AND cg.deletedAt IS NULL
        """,
    )
    fun findByNovelId(
        @Param("novelId") novelId: UUID,
    ): Optional<ContributorGroup>
}
