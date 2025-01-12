package lab.ujumeonji.literaturebackend.domain.contributor

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroup
import org.springframework.data.repository.CrudRepository

interface ContributorGroupRepository : CrudRepository<ContributorGroup, Long>
