package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.common.SoftDeleteable;
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorId;
import org.hibernate.annotations.SQLDelete;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "chapter_authors")
@SQLDelete(sql = "update chapter_authors set deleted_at = current_timestamp where id = ?")
@SoftDeleteable
public class ChapterAuthor extends BaseEntity<UUID> {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "chapter_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_chapter_author_chapter"))
  private Chapter chapter;

  @Column(name = "contributor_id", nullable = false)
  private UUID contributorId;

  @Column(name = "account_id", nullable = false)
  private UUID accountId;

  @Column(nullable = false)
  private boolean isCurrentWriter;

  @Column private Integer writingOrder;

  protected ChapterAuthor() {}

  ChapterAuthor(
      @NotNull Chapter chapter,
      @NotNull ContributorId contributorId,
      @NotNull AccountId accountId,
      boolean isCurrentWriter,
      Integer writingOrder,
      @NotNull LocalDateTime createdAt,
      @NotNull LocalDateTime updatedAt,
      LocalDateTime deletedAt) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.chapter = chapter;
    this.contributorId = contributorId.getId();
    this.accountId = accountId.getId();
    this.isCurrentWriter = isCurrentWriter;
    this.writingOrder = writingOrder;

    setCreatedAt(createdAt);
    setUpdatedAt(updatedAt);
    setDeletedAt(deletedAt);
  }

  public static ChapterAuthor create(
      @NotNull Chapter chapter,
      @NotNull ContributorId contributorId,
      @NotNull AccountId accountId,
      boolean isCurrentWriter,
      Integer writingOrder,
      @NotNull LocalDateTime now) {
    return new ChapterAuthor(
        chapter, contributorId, accountId, isCurrentWriter, writingOrder, now, now, null);
  }

  @Override
  public UUID getId() {
    return id;
  }

  ChapterAuthorId getIdValue() {
    return ChapterAuthorId.from(this.id);
  }

  ContributorId getContributorId() {
    return ContributorId.from(this.contributorId);
  }

  AccountId getAccountId() {
    return AccountId.from(this.accountId);
  }

  boolean isCurrentWriter() {
    return isCurrentWriter;
  }

  void markAsCurrentWriter() {
    this.isCurrentWriter = true;
  }

  void unmarkAsCurrentWriter() {
    this.isCurrentWriter = false;
  }

  int getWritingOrder() {
    return writingOrder;
  }
}
