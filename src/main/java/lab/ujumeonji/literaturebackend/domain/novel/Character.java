package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "characters")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String profileImage;

    @Column
    private Long lastUpdatedBy;

    @ManyToOne
    @JoinColumn(name = "novel_id")
    private Novel novel;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    protected Character() {
    }

    Character(String name, String description, String profileImage, Long lastUpdatedBy, Novel novel,
              LocalDateTime createdAt, LocalDateTime updatedAt,
              LocalDateTime deletedAt) {
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
        this.lastUpdatedBy = lastUpdatedBy;
        this.novel = novel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;

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

    public static Character create(Novel novel, String name, String description, String profileImage,
                                   LocalDateTime now) {
        return new Character(name, description, profileImage, null, novel, now, now, null);
    }

    @Nullable
    public Long getId() {
        return id;
    }

    @Nonnull
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
        if (updatedAt.equals(createdAt)) {
            return null;
        }

        return updatedAt;
    }
}
