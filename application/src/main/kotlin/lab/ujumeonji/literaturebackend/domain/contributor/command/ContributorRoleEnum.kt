package lab.ujumeonji.literaturebackend.domain.contributor.command

enum class ContributorRoleEnum(val alias: String) {
    MAIN("대표 작가"),
    COLLABORATOR("참여 작가"),
    ;

    fun toContributorRole(): lab.ujumeonji.literaturebackend.domain.contributor.ContributorRole {
        return lab.ujumeonji.literaturebackend.domain.contributor.ContributorRole.valueOf(this.name)
    }

    companion object {
        fun fromContributorRole(role: lab.ujumeonji.literaturebackend.domain.contributor.ContributorRole): ContributorRoleEnum {
            return valueOf(role.name)
        }
    }
}
