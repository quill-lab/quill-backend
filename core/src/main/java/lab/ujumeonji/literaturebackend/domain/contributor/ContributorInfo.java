package lab.ujumeonji.literaturebackend.domain.contributor;

import java.time.LocalDateTime;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import org.jetbrains.annotations.NotNull;

public class ContributorInfo {
  private final ContributorId contributorId;
  private final AccountId accountId;
  private final int writingOrder;
  private final LocalDateTime createdAt;
  private final ContributorRole role;

  private ContributorInfo(
      @NotNull ContributorId contributorId,
      @NotNull AccountId accountId,
      int writingOrder,
      LocalDateTime createdAt,
      ContributorRole role) {
    this.contributorId = contributorId;
    this.accountId = accountId;
    this.writingOrder = writingOrder;
    this.createdAt = createdAt;
    this.role = role;
  }

  public static ContributorInfo from(@NotNull Contributor contributor) {
    return new ContributorInfo(
        contributor.getIdValue(),
        contributor.getAccountId(),
        contributor.getWritingOrder(),
        contributor.getCreatedAt(),
        contributor.getRole());
  }

  public ContributorId getContributorId() {
    return contributorId;
  }

  public AccountId getAccountId() {
    return accountId;
  }

  public int getWritingOrder() {
    return writingOrder;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public ContributorRole getRole() {
    return role;
  }
}
