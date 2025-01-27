package lab.ujumeonji.literaturebackend.domain.contributor.command

import lab.ujumeonji.literaturebackend.api.novel.dto.NovelRoomSortType

data class FindContributorGroupsCommand(
    val page: Int,
    val size: Int,
    val sort: NovelRoomSortType = NovelRoomSortType.LATEST,
)
