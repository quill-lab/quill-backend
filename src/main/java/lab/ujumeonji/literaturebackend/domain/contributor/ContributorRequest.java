package lab.ujumeonji.literaturebackend.domain.contributor;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.novel.NovelId;

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
    private UUID novelId;

    @Column(nullable = false)
    private UUID accountId;

    @Column
    @Enumerated(EnumType.STRING)
    private ContributorRequestStatus status;

    protected ContributorRequest() {
    }

    ContributorRequest(@Nonnull ContributorGroup contributorGroup, @Nonnull NovelId novelId, @Nonnull AccountId accountId, ContributorRequestStatus status,
                       LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.contributorGroup = contributorGroup;
        this.novelId = novelId.getId();
        this.accountId = accountId.getId();
        this.status = status;

        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);

        validate();
    }

    private void validate() {
        if (contributorGroup == null) {
            throw new IllegalArgumentException("기여자 그룹은 필수입니다");
        }

        if (novelId == null) {
            throw new IllegalArgumentException("소설 ID는 필수입니다");
        }
    }

    public ContributorRequestId getId() {
        return ContributorRequestId.from(this.id);
    }
}
