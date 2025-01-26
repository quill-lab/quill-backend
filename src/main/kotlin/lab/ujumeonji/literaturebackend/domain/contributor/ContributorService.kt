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
    ): ContributorGroup =
        this.contributorGroupRepository.save(
            ContributorGroup.create(
                command.ownerId,
                command.maxContributorCount,
                command.novelId,
                now,
            )
        )

    fun findByAccountIdWithPaging(accountId: Long, page: Int, size: Int): Pair<List<ContributorGroup>, Long> {
        val pageable = PageRequest.of(page, size)
        val result = contributorGroupRepository.findByAccountId(accountId, pageable)
        return result.content to result.totalElements
    }

    fun findGroupById(id: Long): ContributorGroup? =
        contributorGroupRepository.findById(id).orElse(null)

    fun hasOwnContributorGroup(accountId: Long): Boolean =
        contributorRepository.findAllByAccountIdAndRole(accountId).isNotEmpty()
}
