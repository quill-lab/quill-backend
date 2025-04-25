package lab.ujumeonji.literaturebackend.domain.contributor

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.command.CreateContributorGroupCommand
import lab.ujumeonji.literaturebackend.domain.contributor.command.FindContributorGroupsCommand
import lab.ujumeonji.literaturebackend.domain.contributor.command.FindContributorRequestHistoriesCommand
import lab.ujumeonji.literaturebackend.domain.contributor.command.NovelRoomSortTypeEnum
import lab.ujumeonji.literaturebackend.domain.contributor.dto.ContributorRequestHistory
import lab.ujumeonji.literaturebackend.domain.novel.NovelId
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class ContributorService(
    private val contributorGroupRepository: ContributorGroupRepository,
    private val contributorViewRepository: ContributorViewRepository,
) {
    fun findContributorRequestHistories(
        accountId: AccountId,
        command: FindContributorRequestHistoriesCommand,
    ): Page<ContributorRequestHistory> {
        val pageable =
            PageRequest.of(
                command.page,
                command.size,
                Sort.by(Sort.Direction.DESC, "createdAt"),
            )
        return contributorViewRepository.findContributorRequestsByAccountId(accountId.id, pageable)
    }

    fun findContributorGroups(
        accountId: AccountId,
        command: FindContributorGroupsCommand,
    ): Page<ContributorGroup> {
        val sortDirection =
            when (command.sort) {
                NovelRoomSortTypeEnum.LATEST -> Sort.Direction.DESC
                NovelRoomSortTypeEnum.OLDEST -> Sort.Direction.ASC
            }
        val pageable =
            PageRequest.of(
                command.page,
                command.size,
                Sort.by(sortDirection, "createdAt"),
            )
        return contributorGroupRepository.findByAccountId(accountId.id, pageable)
    }

    @Transactional
    fun createContributorGroup(
        command: CreateContributorGroupCommand,
        now: LocalDateTime = LocalDateTime.now(),
    ): ContributorGroup =
        this.contributorGroupRepository.save(
            ContributorGroup.create(
                command.ownerId,
                command.maxContributorCount,
                command.novelId,
                now,
            ),
        )

    fun findGroupById(id: ContributorGroupId): ContributorGroup? =
        contributorGroupRepository.findById(id.id).orElse(null)

    fun findGroupByNovelId(id: NovelId): ContributorGroup? =
        contributorGroupRepository.findByNovelId(id.id).orElse(null)
}
