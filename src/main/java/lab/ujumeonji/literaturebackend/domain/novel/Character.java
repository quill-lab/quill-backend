package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "characters")
public class Character extends BaseEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String profileImage;

    @Column(name = "last_updated_by")
    private UUID lastUpdatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @Column
    private Integer priority;

    protected Character() {
    }

    Character(String name, String description, String profileImage, @Nullable AccountId lastUpdatedBy, Novel novel,
              Integer priority, LocalDateTime createdAt, LocalDateTime updatedAt,
              LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
        this.lastUpdatedBy = lastUpdatedBy != null ? lastUpdatedBy.getId() : null;
        this.novel = novel;
        this.priority = priority;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);

        validate();
    }

    private void validate() {
    }

    static Character create(@Nonnull Novel novel, @Nonnull String name, @Nonnull String description,
                            String profileImage,
                            Integer priority, @Nonnull LocalDateTime now) {
        return new Character(name, description, profileImage, null, novel, priority, now, now, null);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProfileImage() {
        return profileImage;
    }

    @Nullable
    public AccountId getLastUpdatedBy() {
        return lastUpdatedBy != null ? AccountId.from(lastUpdatedBy) : null;
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

    public CharacterId getId() {
        return CharacterId.from(this.id);
    }
}
