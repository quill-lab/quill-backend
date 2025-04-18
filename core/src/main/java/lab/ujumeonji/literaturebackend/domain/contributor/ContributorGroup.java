package lab.ujumeonji.literaturebackend.domain.contributor;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.novel.NovelId;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "contributor_groups")
@SQLDelete(sql = "update contributor_groups set deleted_at = current_timestamp where id = ?")
@Where(clause = "deleted_at IS NULL")
public class ContributorGroup extends BaseEntity<UUID> {

    @Id
    private UUID id;

    @Column
    private int maxContributorCount;

    @Column
    private int contributorCount;

    @Column
    private ContributorGroupStatus status;

    @Column(nullable = false)
    private UUID novelId;

    @Column
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "contributorGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contributor> contributors = new ArrayList<>();

    @OneToMany(mappedBy = "contributorGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContributorRequest> contributorRequests = new ArrayList<>();

    protected ContributorGroup() {
    }

    ContributorGroup(int maxContributorCount, @Nonnull NovelId novelId, @Nonnull LocalDateTime createdAt,
                     @Nonnull LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.contributorCount = 0;
        this.maxContributorCount = maxContributorCount;
        this.status = ContributorGroupStatus.PREPARING;
        this.novelId = novelId.getId();
        this.completedAt = null;

        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);

        validate();
    }

    static ContributorGroup create(@Nonnull AccountId accountId, int maxContributorCount, @Nonnull NovelId novelId,
                                   @Nonnull LocalDateTime now) {
        ContributorGroup createdContributorGroup = new ContributorGroup(maxContributorCount, novelId, now, now, null);

        createdContributorGroup.addContributor(accountId, ContributorRole.MAIN, now);

        return createdContributorGroup;
    }

    private void validate() {
        if (maxContributorCount <= 0) {
            throw new IllegalArgumentException("최대 기여자 수는 0보다 커야 합니다");
        }

        if (maxContributorCount > 100) {
            throw new IllegalArgumentException("최대 기여자 수는 100을 초과할 수 없습니다");
        }

        if (contributorCount < 0) {
            throw new IllegalArgumentException("현재 기여자 수는 0보다 작을 수 없습니다");
        }

        if (contributorCount > maxContributorCount) {
            throw new IllegalArgumentException("현재 기여자 수는 최대 기여자 수를 초과할 수 없습니다");
        }

        if (status == null) {
            throw new IllegalArgumentException("상태는 필수입니다");
        }
    }

    private void addContributor(AccountId accountId, ContributorRole role, LocalDateTime now) {
        if (contributorCount >= maxContributorCount) {
            throw new IllegalStateException("최대 기여자 수를 초과했습니다");
        }

        Contributor contributor = Contributor.create(accountId, this, role, contributorCount, now);
        contributor.setCurrentWriter();
        contributors.add(contributor);
        contributorCount++;
    }

    @Override
    public UUID getId() {
        return id;
    }

    public ContributorGroupStatus getStatus() {
        return status;
    }

    public NovelId getNovelId() {
        return NovelId.from(novelId);
    }

    public Integer getMaxContributorCount() {
        return maxContributorCount;
    }

    public Integer getContributorCount() {
        return contributorCount;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public boolean hasManagePermission(@Nonnull AccountId accountId) {
        return contributors.stream()
                .filter(contributor -> !contributor.isDeleted())
                .anyMatch(contributor -> contributor.getAccountId().equals(accountId) &&
                        contributor.getRole() == ContributorRole.MAIN);
    }

    public void updateWritingOrder(@Nonnull ContributorId contributorId, int writingOrder) {
        contributors.stream()
                .filter(c -> c.getIdValue().equals(contributorId))
                .findFirst()
                .ifPresent(contributor -> {
                    if (contributor.isDeleted()) {
                        return;
                    }

                    int currentOrder = contributor.getWritingOrder();
                    int maxOrder = (int) contributors.stream()
                            .filter(c -> !c.isDeleted())
                            .count() - 1;

                    int targetOrder = Math.min(writingOrder, maxOrder);

                    if (targetOrder < currentOrder) {
                        shiftOrdersUp(contributorId, targetOrder, currentOrder);
                    } else if (targetOrder > currentOrder) {
                        shiftOrdersDown(contributorId, currentOrder, targetOrder);
                    }

                    contributor.updateWritingOrder(targetOrder);
                });
    }

    private void shiftOrdersUp(@Nonnull ContributorId contributorId, int targetOrder, int currentOrder) {
        contributors.stream()
                .filter(c -> !c.getIdValue().equals(contributorId)
                        && c.getWritingOrder() >= targetOrder
                        && c.getWritingOrder() < currentOrder)
                .forEach(c -> c.updateWritingOrder(c.getWritingOrder() + 1));
    }

    private void shiftOrdersDown(@Nonnull ContributorId contributorId, int currentOrder, int targetOrder) {
        contributors.stream()
                .filter(c -> !c.getIdValue().equals(contributorId)
                        && c.getWritingOrder() <= targetOrder
                        && c.getWritingOrder() > currentOrder)
                .forEach(c -> c.updateWritingOrder(c.getWritingOrder() - 1));
    }

    public boolean isParticipating(@Nonnull AccountId accountId) {
        return contributors.stream()
                .anyMatch(contributor -> contributor.getAccountId().equals(accountId) && !contributor.isDeleted());
    }

    public ContributorGroupId getIdValue() {
        return ContributorGroupId.from(this.id);
    }

    @Nullable
    public ContributorRole getCollaboratorRole(@Nonnull AccountId accountId) {
        return contributors.stream()
                .filter(contributor -> contributor.getAccountId().equals(accountId))
                .map(Contributor::getRole)
                .findFirst()
                .orElse(null);
    }

    @Nullable
    private Contributor getCurrentContributor() {
        return contributors.stream()
                .filter(Contributor::isCurrentWriter)
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public AccountId getActiveContributorAccountId() {
        Contributor currentContributor = getCurrentContributor();
        return currentContributor != null ? currentContributor.getAccountId() : null;
    }

    public boolean isCurrentWriter(@Nonnull AccountId accountId) {
        Contributor currentContributor = getCurrentContributor();
        return currentContributor != null && currentContributor.getAccountId().equals(accountId);
    }

    public void advanceTurn() {
        Contributor currentContributor = getCurrentContributor();
        if (currentContributor == null) {
            contributors.stream()
                    .filter(c -> !c.isDeleted())
                    .min(Comparator.comparingInt(Contributor::getWritingOrder))
                    .ifPresent(Contributor::setCurrentWriter);
            return;
        }

        currentContributor.unsetCurrentWriter();

        int nextWritingOrder = (currentContributor.getWritingOrder() + 1) % contributorCount;

        Contributor nextContributor = contributors.stream()
                .filter(c -> !c.isDeleted() && c.getWritingOrder() == nextWritingOrder)
                .findFirst()
                .orElse(null);

        if (nextContributor == null) {
            int searchOrder = (nextWritingOrder + 1) % contributorCount;
            while (searchOrder != currentContributor.getWritingOrder()) {
                int finalSearchOrder = searchOrder;
                Contributor potentialNext = contributors.stream()
                        .filter(c -> !c.isDeleted() && c.getWritingOrder() == finalSearchOrder)
                        .findFirst()
                        .orElse(null);
                if (potentialNext != null) {
                    nextContributor = potentialNext;
                    break;
                }
                searchOrder = (searchOrder + 1) % contributorCount;
            }
            if (nextContributor == null) {
                nextContributor = currentContributor;
            }
        }

        nextContributor.setCurrentWriter();
    }
}
