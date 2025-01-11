package lab.ujumeonji.literaturebackend.api.novel.dto

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupStatus

data class NovelRoomsQueryRequest(
    val status: ContributorGroupStatus
)
