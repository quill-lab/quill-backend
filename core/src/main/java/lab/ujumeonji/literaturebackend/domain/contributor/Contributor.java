package lab.ujumeonji.literaturebackend.domain.contributor;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contributors")
public class Contributor extends BaseEntity<UUID> {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID accountId;

    @Column
    @Enumerated(EnumType.STRING)
    private ContributorRole role;

    @Column
    private Integer writingOrder;

    @Column
    private boolean isCurrentWriter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contributor_group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contributor_contributor_group"))
    private ContributorGroup contributorGroup;

    protected Contributor() {
    }

    Contributor(@Nonnull AccountId accountId, @Nonnull ContributorGroup contributorGroup, @Nonnull ContributorRole role,
                int writingOrder,
                @Nonnull LocalDateTime createdAt,
                @Nonnull LocalDateTime updatedAt,
                LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.accountId = accountId.getId();
        this.contributorGroup = contributorGroup;
        this.role = role;
        this.writingOrder = writingOrder;
        this.isCurrentWriter = false;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static Contributor create(@Nonnull AccountId accountId, @Nonnull ContributorGroup contributorGroup,
                              @Nonnull ContributorRole role,
                              int writingOrder,
                              @Nonnull LocalDateTime now) {
        return new Contributor(accountId, contributorGroup, role, writingOrder, now, now, null);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public AccountId getAccountId() {
        return AccountId.from(accountId);
    }

    public ContributorRole getRole() {
        return role;
    }

    public Integer getWritingOrder() {
        return writingOrder;
    }

    boolean isDeleted() {
        return getDeletedAt() != null;
    }

    boolean isCurrentWriter() {
        return isCurrentWriter;
    }

    void updateWritingOrder(Integer writingOrder) {
        this.writingOrder = writingOrder;
    }

    ContributorId getIdValue() {
        return ContributorId.from(this.id);
    }

    void setCurrentWriter() {
        this.isCurrentWriter = true;
    }
}
