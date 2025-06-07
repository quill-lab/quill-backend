package lab.ujumeonji.literaturebackend.domain.contributor;

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
@Table(name = "contributor_requests")
@SQLDelete(sql = "UPDATE contributor_requests SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class ContributorRequest extends BaseEntity<UUID> {

  @Id private UUID id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "contributor_group_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_contributor_request_contributor_group"))
  private ContributorGroup contributorGroup;

  @Column(nullable = false)
  private UUID accountId;

  @Column
  @Enumerated(EnumType.STRING)
  private ContributorRequestStatus status;

  @Column private LocalDateTime approvedAt;

  @Column private LocalDateTime rejectedAt;

  protected ContributorRequest() {}

  ContributorRequest(
      @Nonnull ContributorGroup contributorGroup,
      @Nonnull AccountId accountId,
      LocalDateTime approvedAt,
      ContributorRequestStatus status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      LocalDateTime deletedAt) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.contributorGroup = contributorGroup;
    this.accountId = accountId.getId();
    this.status = status;
    this.approvedAt = approvedAt;

    setCreatedAt(createdAt);
    setUpdatedAt(updatedAt);
    setDeletedAt(deletedAt);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public ContributorRequestId getIdValue() {
    return ContributorRequestId.from(this.id);
  }

  public AccountId getAccountId() {
    return AccountId.from(accountId);
  }

  public ContributorRequestStatus getStatus() {
    return status;
  }

  public void approve(LocalDateTime approvedAt) {
    if (this.status != ContributorRequestStatus.REQUESTED) {
      return;
    }
    this.status = ContributorRequestStatus.APPROVED;
    this.approvedAt = approvedAt;
  }

  public void reject(LocalDateTime rejectedAt) {
    if (this.status != ContributorRequestStatus.REQUESTED) {
      return;
    }
    this.status = ContributorRequestStatus.REJECTED;
    this.rejectedAt = rejectedAt;
  }
}
