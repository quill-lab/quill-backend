package lab.ujumeonji.literaturebackend.domain.contributor

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
    ): ContributorGroup {
        val contributorGroup = this.contributorGroupRepository.save(
            ContributorGroup.create(
                command.maxContributorCount,
                command.novelId,
                now,
            )
        )

        contributorGroup.addHostContributor(command.ownerId, now)

        return contributorGroup;
    }

    fun findByAccountIdWithPaging(accountId: Long, page: Int, size: Int): Pair<List<ContributorGroup>, Long> {
        val pageable = PageRequest.of(page, size)
        val result = contributorGroupRepository.findByAccountId(accountId, pageable)
        return result.content to result.totalElements
    }

    fun findGroupById(id: Long): ContributorGroup? =
        contributorGroupRepository.findById(id).orElse(null)

    fun hasManagePermission(contributorGroupId: Long, accountId: Long): Boolean {
        val contributorGroup = findGroupById(contributorGroupId) ?: return false
        return contributorGroup.contributors.any { contributor ->
            contributor.accountId == accountId && contributor.role == ContributorRole.MAIN
        }
    }

    fun hasOwnContributorGroup(accountId: Long): Boolean =
        contributorRepository.findAllByAccountIdAndRole(accountId).isNotEmpty()
}
