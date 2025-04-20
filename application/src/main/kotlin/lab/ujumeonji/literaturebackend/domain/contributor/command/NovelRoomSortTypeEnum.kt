package lab.ujumeonji.literaturebackend.domain.contributor.command

enum class NovelRoomSortTypeEnum {
    LATEST,
    OLDEST,
    ;

    companion object {
        fun from(value: String?): NovelRoomSortTypeEnum {
            return when (value?.uppercase()) {
                "LATEST" -> LATEST
                "OLDEST" -> OLDEST
                null -> LATEST
                else -> throw IllegalArgumentException("Invalid sort type: $value")
            }
        }
    }
}
