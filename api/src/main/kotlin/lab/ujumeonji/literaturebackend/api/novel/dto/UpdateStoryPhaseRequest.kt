package lab.ujumeonji.literaturebackend.api.novel.dto

data class UpdateStoryPhaseRequest(
    val startChapterNumber: Int?,
    val endChapterNumber: Int?,
    val description: String?,
)
