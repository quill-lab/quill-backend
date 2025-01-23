package lab.ujumeonji.literaturebackend.domain.contributor;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "contributor_requests")
public class ContributorRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contributor_group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contributor_request_contributor_group"))
    private ContributorGroup contributorGroup;

    @Column(nullable = false)
    private Long novelId;

    @Column(nullable = false)
    private Long accountId;

    @Column
    @Enumerated(EnumType.STRING)
    private ContributorRequestStatus status;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    protected ContributorRequest() {
    }

    public ContributorRequest(Long id, ContributorGroup contributorGroup, Long novelId, Long accountId, ContributorRequestStatus status, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = id;
        this.contributorGroup = contributorGroup;
        this.novelId = novelId;
        this.accountId = accountId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

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
}
