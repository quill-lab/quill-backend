package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "characters")
public class Character extends BaseEntity {

    @Id
    private UUID id;

    @Column
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String profileImage;

    @Column
    private Long lastUpdatedBy;

    @Column
    private Integer priority;

    @ManyToOne
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    protected Character() {
    }

    Character(String name, String description, String profileImage, Long lastUpdatedBy, Novel novel,
              Integer priority, LocalDateTime createdAt, LocalDateTime updatedAt,
              LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
        this.lastUpdatedBy = lastUpdatedBy;
        this.novel = novel;
        this.priority = priority;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);

        validate();
    }

    private void validate() {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (novel == null) {
            throw new IllegalArgumentException("Novel cannot be null");
        }
    }

    static Character create(Novel novel, String name, String description, String profileImage,
                            Integer priority, LocalDateTime now) {
        return new Character(name, description, profileImage, null, novel, priority, now, now, null);
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getProfileImage() {
        return profileImage;
    }

    @Nullable
    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    @Nullable
    public LocalDateTime getUpdatedAt() {
        if (super.getUpdatedAt().equals(getCreatedAt())) {
            return null;
        }

        return super.getUpdatedAt();
    }

    public Integer getPriority() {
        return priority;
    }

    public UUID getId() {
        return id;
    }
}
