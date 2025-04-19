package lab.ujumeonji.literaturebackend.domain.contributor;

import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import org.jetbrains.annotations.NotNull;

public class ContributorInfo {
    private final ContributorId contributorId;
    private final AccountId accountId;

    private ContributorInfo(@NotNull ContributorId contributorId, @NotNull AccountId accountId) {
        this.contributorId = contributorId;
        this.accountId = accountId;
    }

    public static ContributorInfo from(@NotNull Contributor contributor) {
        return new ContributorInfo(contributor.getIdValue(), contributor.getAccountId());
    }

    public ContributorId getContributorId() {
        return contributorId;
    }

    public AccountId getAccountId() {
        return accountId;
    }
}
