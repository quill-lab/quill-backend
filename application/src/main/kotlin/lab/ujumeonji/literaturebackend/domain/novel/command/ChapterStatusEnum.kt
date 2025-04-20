package lab.ujumeonji.literaturebackend.domain.novel.command

enum class ChapterStatusEnum {
    DRAFT,
    IN_PROGRESS,
    REQUESTED,
    APPROVED,
    CANCELLED,
    REJECTED,
    ;

    fun toChapterStatus(): lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus {
        return when (this) {
            DRAFT -> lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.DRAFT
            IN_PROGRESS -> lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.IN_PROGRESS
            REQUESTED -> lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.REQUESTED
            APPROVED -> lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.APPROVED
            CANCELLED -> lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.CANCELLED
            REJECTED -> lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.REJECTED
        }
    }

    companion object {
        fun fromChapterStatus(status: lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus): ChapterStatusEnum {
            return when (status) {
                lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.DRAFT -> DRAFT
                lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.IN_PROGRESS -> IN_PROGRESS
                lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.REQUESTED -> REQUESTED
                lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.APPROVED -> APPROVED
                lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.CANCELLED -> CANCELLED
                lab.ujumeonji.literaturebackend.domain.novel.ChapterStatus.REJECTED -> REJECTED
            }
        }
    }
}
