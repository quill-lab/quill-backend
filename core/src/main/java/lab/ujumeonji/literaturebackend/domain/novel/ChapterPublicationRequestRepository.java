package lab.ujumeonji.literaturebackend.domain.novel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterPublicationRequestRepository
    extends JpaRepository<ChapterPublicationRequest, UUID> {

  List<ChapterPublicationRequest> findByChapter_Id(UUID chapterId);

  List<ChapterPublicationRequest> findByChapter_IdAndStatus(
      UUID chapterId, ChapterPublicationRequestStatus status);

  Optional<ChapterPublicationRequest> findFirstByChapter_IdOrderByCreatedAtDesc(UUID chapterId);

  Optional<ChapterPublicationRequest> findFirstByChapter_IdAndStatusOrderByCreatedAtDesc(
      UUID chapterId, ChapterPublicationRequestStatus status);
}
