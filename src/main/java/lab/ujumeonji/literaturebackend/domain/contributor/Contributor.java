package lab.ujumeonji.literaturebackend.domain.contributor;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "contributors")
class Contributor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long accountId;

    @Column
    @Enumerated(EnumType.STRING)
    private ContributorRole role;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contributor_group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contributor_group"))
    private ContributorGroup contributorGroup;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    protected Contributor() {
    }

    Contributor(Long accountId, ContributorGroup contributorGroup, LocalDateTime createdAt, LocalDateTime updatedAt,
                LocalDateTime deletedAt) {
        this.accountId = accountId;
        this.contributorGroup = contributorGroup;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    static Contributor create(Long accountId, ContributorGroup contributorGroup, LocalDateTime now) {
        return new Contributor(accountId, contributorGroup, now, now, null);
    }

    Long getAccountId() {
        return accountId;
    }

    ContributorRole getRole() {
        return role;
    }
}
