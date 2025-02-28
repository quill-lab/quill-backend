package lab.ujumeonji.literaturebackend.domain.contributor.command

data class FindContributorGroupsCommand(
    val page: Int,
    val size: Int,
    val sort: NovelRoomSortTypeEnum = NovelRoomSortTypeEnum.LATEST,
)
