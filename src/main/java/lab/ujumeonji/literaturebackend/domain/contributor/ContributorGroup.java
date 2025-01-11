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
    private Integer maxMemberCount;

    @Column
    private Integer currentMemberCount;

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

    ContributorGroup(String name, String description, Integer maxMemberCount, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.name = name;
        this.description = description;
        this.maxMemberCount = maxMemberCount;
        this.currentMemberCount = 0;
        this.status = ContributorGroupStatus.PREPARING;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static ContributorGroup create(String name, String description, Integer maxMemberCount, LocalDateTime now) {
        return new ContributorGroup(name, description, maxMemberCount, now, now, null);
    }
}
