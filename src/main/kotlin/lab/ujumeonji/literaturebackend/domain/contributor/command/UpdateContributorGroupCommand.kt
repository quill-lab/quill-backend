package lab.ujumeonji.literaturebackend.domain.contributor.command

data class UpdateContributorGroupCommand(
    val name: String?,
    val description: String?,
    val tags: List<String>?,
    val maxContributorCount: Int?,
)
