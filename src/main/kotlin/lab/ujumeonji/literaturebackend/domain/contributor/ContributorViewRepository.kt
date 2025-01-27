package lab.ujumeonji.literaturebackend.domain.contributor

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import lab.ujumeonji.literaturebackend.domain.contributor.QContributorGroup.contributorGroup
import lab.ujumeonji.literaturebackend.domain.contributor.QContributorRequest.contributorRequest
import lab.ujumeonji.literaturebackend.domain.contributor.dto.ContributorRequestHistory
import lab.ujumeonji.literaturebackend.domain.novel.QNovel.novel
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class ContributorViewRepository(
    private val queryFactory: JPAQueryFactory
) {
    fun findContributorRequestsByAccountId(accountId: UUID, pageable: Pageable): Page<ContributorRequestHistory> {
        val totalCount = queryFactory
            .select(contributorRequest.count())
            .from(contributorRequest)
            .where(contributorRequest.accountId.eq(accountId))
            .fetchOne() ?: 0L

        val results = queryFactory
            .select(
                Projections.constructor(
                    ContributorRequestHistory::class.java,
                    contributorRequest.id,
                    novel.title,
                    contributorRequest.status,
                    contributorRequest.createdAt,
                    contributorRequest.approvedAt,
                    contributorGroup.deletedAt
                )
            )
            .from(contributorRequest)
            .join(contributorRequest.contributorGroup, contributorGroup)
            .join(novel).on(novel.id.eq(contributorGroup.novelId))
            .where(contributorRequest.accountId.eq(accountId))
            .orderBy(contributorRequest.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        return PageImpl(results, pageable, totalCount)
    }
}
