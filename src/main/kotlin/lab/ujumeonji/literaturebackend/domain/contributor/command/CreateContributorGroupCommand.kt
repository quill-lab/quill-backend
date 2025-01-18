package lab.ujumeonji.literaturebackend.domain.contributor.command

data class CreateContributorGroupCommand(
    val novelId: Long,
    val ownerId: Long,
    val tags: List<String>,
    val maxContributorCount: Int,
)
