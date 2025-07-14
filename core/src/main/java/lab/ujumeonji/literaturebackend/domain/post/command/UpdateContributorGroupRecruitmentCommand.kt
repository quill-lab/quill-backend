package lab.ujumeonji.literaturebackend.domain.post.command

data class UpdateContributorGroupRecruitmentCommand(
    val title: String,
    val content: String,
    val link: String,
)
