package lab.ujumeonji.literaturebackend.api.novel.dto

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
