package lab.ujumeonji.literaturebackend.domain.post.command

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId

data class CreateNovelRoomRecruitmentPostCommand(
    val title: String,
    val content: String,
    val link: String,
    val authorId: AccountId,
    val contributorGroupId: ContributorGroupId,
)
