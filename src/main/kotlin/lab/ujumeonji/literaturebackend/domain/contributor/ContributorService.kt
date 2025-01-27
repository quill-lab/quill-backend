package lab.ujumeonji.literaturebackend.domain.contributor

import lab.ujumeonji.literaturebackend.api.novel.dto.NovelRoomSortType
import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.command.CreateContributorGroupCommand
import lab.ujumeonji.literaturebackend.domain.contributor.dto.ContributorRequestHistory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class ContributorService(
    private val contributorGroupRepository: ContributorGroupRepository,
    private val contributorRepository: ContributorRepository,
    private val contributorViewRepository: ContributorViewRepository
) {

    fun findContributorRequestHistories(
        accountId: AccountId,
        pageable: Pageable
    ): Page<ContributorRequestHistory> {
        return contributorViewRepository.findContributorRequestsByAccountId(accountId.id, pageable)
    }

    @Transactional
    fun createContributorGroup(
        command: CreateContributorGroupCommand,
        now: LocalDateTime = LocalDateTime.now()
    ): ContributorGroup =
        this.contributorGroupRepository.save(
            ContributorGroup.create(
                command.ownerId,
                command.maxContributorCount,
                command.novelId,
                now,
            )
        )

    fun findByAccountIdWithPaging(
        accountId: AccountId,
        page: Int,
        size: Int,
        sort: NovelRoomSortType = NovelRoomSortType.LATEST
    ): Pair<List<ContributorGroup>, Long> {
        val sortDirection = when (sort) {
            NovelRoomSortType.LATEST -> Sort.Direction.DESC
            NovelRoomSortType.OLDEST -> Sort.Direction.ASC
        }
        val pageable = PageRequest.of(page, size, Sort.by(sortDirection, "createdAt"))
        val result = contributorGroupRepository.findByAccountId(accountId.id, pageable)
        return result.content to result.totalElements
    }

    fun findGroupById(id: ContributorGroupId): ContributorGroup? =
        contributorGroupRepository.findById(id.id).orElse(null)

    fun hasOwnContributorGroup(accountId: AccountId): Boolean =
        contributorRepository.findAllByAccountIdAndRole(accountId.id).isNotEmpty()
}
