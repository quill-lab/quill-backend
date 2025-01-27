package lab.ujumeonji.literaturebackend.domain.contributor

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Root
import lab.ujumeonji.literaturebackend.domain.contributor.dto.ContributorRequestHistory
import lab.ujumeonji.literaturebackend.domain.novel.Novel
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
class ContributorViewRepository(
    private val entityManager: EntityManager
) {
    fun findContributorRequestsByAccountId(accountId: UUID, pageable: Pageable): Page<ContributorRequestHistory> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val totalCount = getTotalCount(accountId, criteriaBuilder)
        val results = getResults(accountId, pageable, criteriaBuilder)
        return PageImpl(results, pageable, totalCount)
    }

    private fun getTotalCount(accountId: UUID, cb: CriteriaBuilder): Long {
        val countQuery = cb.createQuery(Long::class.java)
        val root = countQuery.from(ContributorRequest::class.java)
        val contributorGroup = root.join<ContributorRequest, ContributorGroup>("contributorGroup", JoinType.INNER)
        val novel = contributorGroup.join<ContributorGroup, Novel>("novel", JoinType.INNER)

        countQuery.select(cb.count(root))
            .where(buildWhereClause(accountId, cb, root, contributorGroup, novel))

        return entityManager.createQuery(countQuery).singleResult
    }

    private fun getResults(
        accountId: UUID,
        pageable: Pageable,
        cb: CriteriaBuilder
    ): List<ContributorRequestHistory> {
        val query = cb.createQuery(ContributorRequestHistory::class.java)
        val root = query.from(ContributorRequest::class.java)
        val contributorGroup = root.join<ContributorRequest, ContributorGroup>("contributorGroup", JoinType.INNER)
        val novel = contributorGroup.join<ContributorGroup, Novel>("novel", JoinType.INNER)

        query.select(buildSelect(cb, root, novel, contributorGroup))
            .where(buildWhereClause(accountId, cb, root, contributorGroup, novel))
            .orderBy(cb.desc(root.get<LocalDateTime>("createdAt")))

        return entityManager.createQuery(query)
            .setFirstResult(pageable.offset.toInt())
            .setMaxResults(pageable.pageSize)
            .resultList
    }

    private fun buildSelect(
        cb: CriteriaBuilder,
        root: Root<ContributorRequest>,
        novel: jakarta.persistence.criteria.Join<*, *>,
        contributorGroup: jakarta.persistence.criteria.Join<*, *>
    ) = cb.construct(
        ContributorRequestHistory::class.java,
        root.get<UUID>("id"),
        novel.get<String>("title"),
        root.get<ContributorRequestStatus>("status"),
        root.get<LocalDateTime>("createdAt"),
        root.get<LocalDateTime>("approvedAt"),
        contributorGroup.get<LocalDateTime>("deletedAt")
    )

    private fun buildWhereClause(
        accountId: UUID,
        cb: CriteriaBuilder,
        root: Root<*>,
        contributorGroup: jakarta.persistence.criteria.Join<*, *>,
        novel: jakarta.persistence.criteria.Join<*, *>
    ) = cb.and(
        cb.equal(root.get<UUID>("accountId"), accountId),
        cb.isNull(root.get<LocalDateTime>("deletedAt")),
        cb.isNull(contributorGroup.get<LocalDateTime>("deletedAt")),
        cb.isNull(novel.get<LocalDateTime>("deletedAt"))
    )
}
