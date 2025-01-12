package lab.ujumeonji.literaturebackend.domain.contributor;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contributor_groups")
public class ContributorGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Integer maxContributorCount;

    @Column
    private Integer currentContributorCount;

    @Column
    private ContributorGroupStatus status;

    @OneToMany(mappedBy = "contributorGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contributor> contributors = new ArrayList<>();

    @Column
    private Long novelId;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    protected ContributorGroup() {
    }

    ContributorGroup(String name, String description, int maxContributorCount, long novelId, LocalDateTime createdAt,
                     LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.name = name;
        this.description = description;
        this.maxContributorCount = maxContributorCount;
        this.currentContributorCount = 0;
        this.status = ContributorGroupStatus.PREPARING;
        this.novelId = novelId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

        validate();
    }

    private void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("이름은 100자를 초과할 수 없습니다");
        }

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("설명은 필수입니다");
        }
        if (description.length() > 1000) {
            throw new IllegalArgumentException("설명은 1000자를 초과할 수 없습니다");
        }

        if (maxContributorCount == null) {
            throw new IllegalArgumentException("최대 기여자 수는 필수입니다");
        }
        if (maxContributorCount <= 0) {
            throw new IllegalArgumentException("최대 기여자 수는 0보다 커야 합니다");
        }
        if (maxContributorCount > 100) {
            throw new IllegalArgumentException("최대 기여자 수는 100을 초과할 수 없습니다");
        }

        if (currentContributorCount == null) {
            throw new IllegalArgumentException("현재 기여자 수는 필수입니다");
        }
        if (currentContributorCount < 0) {
            throw new IllegalArgumentException("현재 기여자 수는 0보다 작을 수 없습니다");
        }
        if (currentContributorCount > maxContributorCount) {
            throw new IllegalArgumentException("현재 기여자 수는 최대 기여자 수를 초과할 수 없습니다");
        }

        if (status == null) {
            throw new IllegalArgumentException("상태는 필수입니다");
        }

        if (novelId == null) {
            throw new IllegalArgumentException("소설 ID는 필수입니다");
        }
    }

    public static ContributorGroup create(String name, String description, int maxContributorCount, long novelId,
                                          LocalDateTime now) {
        return new ContributorGroup(name, description, maxContributorCount, novelId, now, now, null);
    }

    public void addHostContributor(long accountId, LocalDateTime now) {
        if (currentContributorCount >= maxContributorCount) {
            throw new IllegalStateException("최대 기여자 수를 초과했습니다");
        }

        Contributor contributor = Contributor.create(accountId, this, now);
        contributors.add(contributor);
        currentContributorCount++;
    }

    public Long getId() {
        return id;
    }
}
