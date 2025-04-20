package lab.ujumeonji.literaturebackend.domain.contributor

import jakarta.persistence.EntityManager
import jakarta.persistence.criteria.JoinType
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
    private val entityManager: EntityManager,
) {
    fun findContributorRequestsByAccountId(
        accountId: UUID,
        pageable: Pageable,
    ): Page<ContributorRequestHistory> {
        val criteriaBuilder = entityManager.criteriaBuilder
        val criteriaQuery = criteriaBuilder.createQuery(ContributorRequestHistory::class.java)
        val root = criteriaQuery.from(ContributorRequest::class.java)

        val contributorGroup = root.join<ContributorRequest, ContributorGroup>("contributorGroup", JoinType.INNER)

        /* TODO: Novel과의 cross join으로 인한 성능 이슈 존재
         * 1. Novel 테이블과의 join 방식을 INNER JOIN으로 변경 필요
         * 2. 현재 Novel과 ContributorGroup이 novelId로 연결되어 있으므로
         *    contributorGroup.get("novelId")를 기준으로 Novel과 join 구문 작성
         * 3. 예상 쿼리:
         *    SELECT cr.*, n.title
         *    FROM contributor_requests cr
         *    INNER JOIN contributor_groups cg ON cr.contributor_group_id = cg.id
         *    INNER JOIN novels n ON cg.novel_id = n.id
         *    WHERE ...
         */
        val novel = criteriaQuery.from(Novel::class.java)

        criteriaQuery.select(
            criteriaBuilder.construct(
                ContributorRequestHistory::class.java,
                root.get<UUID>("id"),
                novel.get<String>("title"),
                root.get<ContributorRequestStatus>("status"),
                root.get<LocalDateTime>("createdAt"),
                root.get<LocalDateTime>("approvedAt"),
                contributorGroup.get<LocalDateTime>("deletedAt"),
            ),
        )

        criteriaQuery.where(
            criteriaBuilder.and(
                criteriaBuilder.equal(root.get<UUID>("accountId"), accountId),
                criteriaBuilder.equal(novel.get<UUID>("id"), contributorGroup.get<UUID>("novelId")),
                criteriaBuilder.isNull(root.get<LocalDateTime>("deletedAt")),
                criteriaBuilder.isNull(contributorGroup.get<LocalDateTime>("deletedAt")),
                criteriaBuilder.isNull(novel.get<LocalDateTime>("deletedAt")),
            ),
        )

        criteriaQuery.orderBy(
            criteriaBuilder.desc(root.get<LocalDateTime>("createdAt")),
        )

        val countQuery = criteriaBuilder.createQuery(Long::class.java)
        val countRoot = countQuery.from(ContributorRequest::class.java)
        val countContributorGroup =
            countRoot.join<ContributorRequest, ContributorGroup>("contributorGroup", JoinType.INNER)
        val countNovel = countQuery.from(Novel::class.java)

        countQuery.select(criteriaBuilder.count(countRoot))
        countQuery.where(
            criteriaBuilder.and(
                criteriaBuilder.equal(countRoot.get<UUID>("accountId"), accountId),
                criteriaBuilder.equal(countNovel.get<UUID>("id"), countContributorGroup.get<UUID>("novelId")),
                criteriaBuilder.isNull(countRoot.get<LocalDateTime>("deletedAt")),
                criteriaBuilder.isNull(countContributorGroup.get<LocalDateTime>("deletedAt")),
                criteriaBuilder.isNull(countNovel.get<LocalDateTime>("deletedAt")),
            ),
        )
        val totalCount = entityManager.createQuery(countQuery).singleResult

        val query = entityManager.createQuery(criteriaQuery)
        query.firstResult = pageable.offset.toInt()
        query.maxResults = pageable.pageSize

        val results = query.resultList

        return PageImpl(results, pageable, totalCount)
    }
}
