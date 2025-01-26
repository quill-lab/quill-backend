package lab.ujumeonji.literaturebackend.domain.contributor

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.command.CreateContributorGroupCommand
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class ContributorService(
    private val contributorGroupRepository: ContributorGroupRepository,
    private val contributorRepository: ContributorRepository
) {

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

    fun findByAccountIdWithPaging(accountId: AccountId, page: Int, size: Int): Pair<List<ContributorGroup>, Long> {
        val pageable = PageRequest.of(page, size)
        val result = contributorGroupRepository.findByAccountId(accountId.id, pageable)
        return result.content to result.totalElements
    }

    fun findGroupById(id: ContributorGroupId): ContributorGroup? =
        contributorGroupRepository.findById(id.id).orElse(null)

    fun hasOwnContributorGroup(accountId: AccountId): Boolean =
        contributorRepository.findAllByAccountIdAndRole(accountId.id).isNotEmpty()
}
