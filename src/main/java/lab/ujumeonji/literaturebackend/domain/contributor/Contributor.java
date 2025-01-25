package lab.ujumeonji.literaturebackend.domain.contributor;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "contributors")
public class Contributor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long accountId;

    @Column
    @Enumerated(EnumType.STRING)
    private ContributorRole role;

    @Column
    private Integer writingOrder;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contributor_group_id", nullable = false, foreignKey = @ForeignKey(name = "fk_contributor_contributor_group"))
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

    Contributor(Long accountId, ContributorGroup contributorGroup, Integer writingOrder, LocalDateTime createdAt,
                LocalDateTime updatedAt,
                LocalDateTime deletedAt) {
        this.accountId = accountId;
        this.contributorGroup = contributorGroup;
        this.writingOrder = writingOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    static Contributor create(Long accountId, ContributorGroup contributorGroup, Integer writingOrder,
                              LocalDateTime now) {
        return new Contributor(accountId, contributorGroup, writingOrder, now, now, null);
    }

    public Long getAccountId() {
        return accountId;
    }

    ContributorRole getRole() {
        return role;
    }

    Long getId() {
        return id;
    }
}
