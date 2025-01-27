package lab.ujumeonji.literaturebackend.domain.contributor.command

data class FindContributorRequestHistoriesCommand(
    val page: Int,
    val size: Int,
)
