package lab.ujumeonji.literaturebackend.domain.novel.command

enum class ChapterPublicationRequestStatusEnum {
    REQUESTED,
    APPROVED,
    REJECTED,
    ;

    fun toChapterPublicationRequestStatus(): lab.ujumeonji.literaturebackend.domain.novel.ChapterPublicationRequestStatus {
        return when (this) {
            REQUESTED -> lab.ujumeonji.literaturebackend.domain.novel.ChapterPublicationRequestStatus.REQUESTED
            APPROVED -> lab.ujumeonji.literaturebackend.domain.novel.ChapterPublicationRequestStatus.APPROVED
            REJECTED -> lab.ujumeonji.literaturebackend.domain.novel.ChapterPublicationRequestStatus.REJECTED
        }
    }

    companion object {
        fun fromChapterPublicationRequestStatus(
            status: lab.ujumeonji.literaturebackend.domain.novel.ChapterPublicationRequestStatus
        ): ChapterPublicationRequestStatusEnum {
            return when (status) {
                lab.ujumeonji.literaturebackend.domain.novel.ChapterPublicationRequestStatus.REQUESTED -> REQUESTED
                lab.ujumeonji.literaturebackend.domain.novel.ChapterPublicationRequestStatus.APPROVED -> APPROVED
                lab.ujumeonji.literaturebackend.domain.novel.ChapterPublicationRequestStatus.REJECTED -> REJECTED
            }
        }
    }
}
