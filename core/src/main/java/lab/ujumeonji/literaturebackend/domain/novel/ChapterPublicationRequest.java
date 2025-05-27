package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "chapter_publication_requests")
@SQLDelete(
    sql = "update chapter_publication_requests set deleted_at = current_timestamp where id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ChapterPublicationRequest extends BaseEntity<UUID> {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "chapter_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_chapter_publication_request_chapter"))
  private Chapter chapter;

  @Column(nullable = false)
  private UUID requesterId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ChapterPublicationRequestStatus status;

  @Column private UUID reviewerId;

  @Column private String comment;

  @Column private LocalDateTime reviewedAt;

  protected ChapterPublicationRequest() {}

  private ChapterPublicationRequest(
      @Nonnull Chapter chapter,
      @Nonnull AccountId requesterId,
      @Nonnull ChapterPublicationRequestStatus status,
      @Nonnull LocalDateTime createdAt,
      @Nonnull LocalDateTime updatedAt) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.chapter = chapter;
    this.requesterId = requesterId.getId();
    this.status = status;

    setCreatedAt(createdAt);
    setUpdatedAt(updatedAt);
  }

  public static ChapterPublicationRequest create(
      @Nonnull Chapter chapter, @Nonnull AccountId requesterId, @Nonnull LocalDateTime now) {
    return new ChapterPublicationRequest(
        chapter, requesterId, ChapterPublicationRequestStatus.REQUESTED, now, now);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public ChapterPublicationRequestId getIdValue() {
    return ChapterPublicationRequestId.from(this.id);
  }

  public ChapterId getChapterId() {
    return this.chapter.getIdValue();
  }

  public AccountId getRequesterId() {
    return AccountId.from(this.requesterId);
  }

  public ChapterPublicationRequestStatus getStatus() {
    return status;
  }

  public AccountId getReviewerId() {
    return reviewerId != null ? AccountId.from(this.reviewerId) : null;
  }

  public String getComment() {
    return comment;
  }

  public LocalDateTime getReviewedAt() {
    return reviewedAt;
  }

  public boolean approve(@Nonnull AccountId reviewerId, @Nonnull LocalDateTime now) {
    if (this.status != ChapterPublicationRequestStatus.REQUESTED) {
      return false;
    }

    this.status = ChapterPublicationRequestStatus.APPROVED;
    this.reviewerId = reviewerId.getId();
    this.reviewedAt = now;
    setUpdatedAt(now);

    return true;
  }

  public boolean approve(
      @Nonnull AccountId reviewerId, String comment, @Nonnull LocalDateTime now) {
    if (!approve(reviewerId, now)) {
      return false;
    }

    this.comment = comment;
    return true;
  }

  public boolean reject(@Nonnull AccountId reviewerId, @Nonnull LocalDateTime now) {
    if (this.status != ChapterPublicationRequestStatus.REQUESTED) {
      return false;
    }

    this.status = ChapterPublicationRequestStatus.REJECTED;
    this.reviewerId = reviewerId.getId();
    this.reviewedAt = now;
    setUpdatedAt(now);

    return true;
  }

  public boolean reject(@Nonnull AccountId reviewerId, String comment, @Nonnull LocalDateTime now) {
    if (!reject(reviewerId, now)) {
      return false;
    }

    this.comment = comment;
    return true;
  }
}
