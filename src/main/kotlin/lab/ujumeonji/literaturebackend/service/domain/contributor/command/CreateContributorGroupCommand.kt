package lab.ujumeonji.literaturebackend.service.domain.contributor.command

data class CreateContributorGroupCommand(
    val novelId: Long,
    val name: String,
    val description: String,
    val maxContributorCount: Int,
)
