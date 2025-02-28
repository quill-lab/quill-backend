package lab.ujumeonji.literaturebackend.domain.contributor.command

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.novel.NovelId

data class CreateContributorGroupCommand(
    val novelId: NovelId,
    val ownerId: AccountId,
    val tags: List<String>,
    val maxContributorCount: Int,
)
