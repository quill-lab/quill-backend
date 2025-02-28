package lab.ujumeonji.literaturebackend.domain.contributor.command

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorRequestStatus

enum class ContributorRequestStatusEnum {
    REQUESTED,
    APPROVED,
    CANCELLED,
    REJECTED;

    companion object {
        fun fromContributorRequestStatus(status: ContributorRequestStatus): ContributorRequestStatusEnum {
            return valueOf(status.name)
        }
    }
}
