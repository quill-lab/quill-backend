package lab.ujumeonji.literaturebackend.domain.contributor;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "contributors")
public class Contributor extends BaseEntity {

    @EmbeddedId
    private ContributorId id;

    @Column(nullable = false)
    private AccountId accountId;

    @Column
    @Enumerated(EnumType.STRING)
    private ContributorRole role;

    @Column
    private Integer writingOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contributor_group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contributor_contributor_group"))
    private ContributorGroup contributorGroup;

    protected Contributor() {
    }

    Contributor(@Nonnull AccountId accountId, @Nonnull ContributorGroup contributorGroup, @Nonnull ContributorRole role, int writingOrder,
                @Nonnull LocalDateTime createdAt,
                @Nonnull LocalDateTime updatedAt,
                LocalDateTime deletedAt) {
        this.id = new ContributorId(UuidCreator.getTimeOrderedEpoch());
        this.accountId = accountId;
        this.contributorGroup = contributorGroup;
        this.role = role;
        this.writingOrder = writingOrder;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static Contributor create(@Nonnull AccountId accountId, @Nonnull ContributorGroup contributorGroup, @Nonnull ContributorRole role,
                              int writingOrder,
                              @Nonnull LocalDateTime now) {
        return new Contributor(accountId, contributorGroup, role, writingOrder, now, now, null);
    }

    public AccountId getAccountId() {
        return accountId;
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

    void updateWritingOrder(Integer writingOrder) {
        this.writingOrder = writingOrder;
    }

    ContributorId getId() {
        return id;
    }
}
