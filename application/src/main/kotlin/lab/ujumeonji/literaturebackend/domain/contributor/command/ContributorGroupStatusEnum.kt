package lab.ujumeonji.literaturebackend.domain.contributor.command

enum class ContributorGroupStatusEnum {
    PREPARING,
    ACTIVE,
    COMPLETED,
    DELETED;

    companion object {
        fun fromContributorGroupStatus(status: lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupStatus): ContributorGroupStatusEnum {
            return valueOf(status.name)
        }
    }
}
