package lab.ujumeonji.literaturebackend.domain.contributor;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "contributor_requests")
public class ContributorRequest extends BaseEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contributor_group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contributor_request_contributor_group"))
    private ContributorGroup contributorGroup;

    @Column(nullable = false)
    private UUID accountId;

    @Column
    @Enumerated(EnumType.STRING)
    private ContributorRequestStatus status;

    @Column
    private LocalDateTime approvedAt;

    protected ContributorRequest() {
    }

    ContributorRequest(@Nonnull ContributorGroup contributorGroup,
                       @Nonnull AccountId accountId, LocalDateTime approvedAt, ContributorRequestStatus status,
                       LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.contributorGroup = contributorGroup;
        this.accountId = accountId.getId();
        this.status = status;
        this.approvedAt = approvedAt;

        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    public ContributorRequestId getId() {
        return ContributorRequestId.from(this.id);
    }
}
