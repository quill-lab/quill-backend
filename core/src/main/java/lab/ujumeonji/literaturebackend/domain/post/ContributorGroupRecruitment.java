package lab.ujumeonji.literaturebackend.domain.post;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "contributor_group_recruitments")
@SQLDelete(
    sql = "UPDATE contributor_group_recruitments SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class ContributorGroupRecruitment extends BaseEntity<UUID> {

  @Id private UUID id;

  private String title;

  private String content;

  private String link;

  private UUID contributorGroupId;

  private UUID authorId;

  @Enumerated(EnumType.STRING)
  private ContributorGroupRecruitmentStatus status;

  protected ContributorGroupRecruitment() {}

  ContributorGroupRecruitment(
      @NotNull final UUID id,
      @NotNull final ContributorGroupId contributorGroupId,
      @NotNull final AccountId authorId,
      @NotNull final String title,
      @NotNull final String content,
      @Nullable final String link,
      @NotNull final LocalDateTime createdAt,
      @NotNull final LocalDateTime updatedAt,
      @Nullable final LocalDateTime deletedAt) {
    this.id = id;
    this.contributorGroupId = contributorGroupId.getId();
    this.authorId = authorId.getId();
    this.title = title;
    this.content = content;
    this.link = link;
    this.status = ContributorGroupRecruitmentStatus.RECRUITING;
    setCreatedAt(createdAt);
    setUpdatedAt(updatedAt);
    setDeletedAt(deletedAt);
  }

  static ContributorGroupRecruitment create(
      @NotNull final ContributorGroupId contributorGroupId,
      @NotNull final AccountId authorId,
      @NotNull final String title,
      @NotNull final String content,
      @Nullable final String link,
      @NotNull final LocalDateTime now) {
    return new ContributorGroupRecruitment(
        UuidCreator.getTimeOrderedEpoch(),
        contributorGroupId,
        authorId,
        title,
        content,
        link,
        now,
        now,
        null);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public ContributorGroupRecruitmentId getIdValue() {
    return ContributorGroupRecruitmentId.from(this.id);
  }

  public ContributorGroupRecruitmentStatus getStatus() {
    return status;
  }
}
