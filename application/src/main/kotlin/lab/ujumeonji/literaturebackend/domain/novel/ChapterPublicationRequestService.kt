package lab.ujumeonji.literaturebackend.domain.novel

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class ChapterPublicationRequestService(
    private val chapterPublicationRequestRepository: ChapterPublicationRequestRepository
) {
    
    fun findByChapterId(chapterId: ChapterId): List<ChapterPublicationRequest> {
        return chapterPublicationRequestRepository.findByChapter_Id(chapterId.id)
    }
    
    fun findByChapterIdAndStatus(chapterId: ChapterId, status: ChapterPublicationRequestStatus): List<ChapterPublicationRequest> {
        return chapterPublicationRequestRepository.findByChapter_IdAndStatus(chapterId.id, status)
    }
    
    fun findLatestByChapterId(chapterId: ChapterId): ChapterPublicationRequest? {
        return chapterPublicationRequestRepository.findFirstByChapter_IdOrderByCreatedAtDesc(chapterId.id).orElse(null)
    }
    
    fun findLatestByChapterIdAndStatus(chapterId: ChapterId, status: ChapterPublicationRequestStatus): ChapterPublicationRequest? {
        return chapterPublicationRequestRepository.findFirstByChapter_IdAndStatusOrderByCreatedAtDesc(chapterId.id, status).orElse(null)
    }
}