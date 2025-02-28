package lab.ujumeonji.literaturebackend.domain.contributor.command

enum class NovelRoomSortType {
    LATEST,
    OLDEST;

    companion object {
        fun from(value: String?): NovelRoomSortType {
            return when (value?.uppercase()) {
                "LATEST" -> LATEST
                "OLDEST" -> OLDEST
                null -> LATEST
                else -> throw IllegalArgumentException("Invalid sort type: $value")
            }
        }
    }
}
