package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "characters")
public class Character extends BaseEntity {

    @EmbeddedId
    private CharacterId id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String profileImage;

    @Column
    private String lastUpdatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @Column
    private Integer priority;

    protected Character() {
    }

    Character(String name, String description, String profileImage, String lastUpdatedBy, Novel novel,
              Integer priority, LocalDateTime createdAt, LocalDateTime updatedAt,
              LocalDateTime deletedAt) {
        this.id = new CharacterId(UuidCreator.getTimeOrderedEpoch());
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
    }

    static Character create(@Nonnull Novel novel, @Nonnull String name, @Nonnull String description, String profileImage,
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

    public String getLastUpdatedBy() {
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

    public CharacterId getId() {
        return id;
    }
}
