package lab.ujumeonji.literaturebackend.service.domain.contributor

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroup
import lab.ujumeonji.literaturebackend.service.domain.contributor.command.CreateContributorGroupCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class ContributorService(
    private val contributorGroupRepository: ContributorGroupRepository
) {

    @Transactional
    fun createContributorGroup(
        command: CreateContributorGroupCommand,
        now: LocalDateTime = LocalDateTime.now()
    ): ContributorGroup = this.contributorGroupRepository.save(
        ContributorGroup.create(
            command.name,
            command.description,
            command.maxContributorCount,
            now,
        )
    )
}
