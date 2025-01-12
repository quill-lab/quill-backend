package lab.ujumeonji.literaturebackend.domain.novel;

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

    Character(String name, String description, String profileImage, LocalDateTime createdAt, LocalDateTime updatedAt,
              LocalDateTime deletedAt) {
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Character create(String name, String description, String profileImage, LocalDateTime now) {
        return new Character(name, description, profileImage, now, now, null);
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
}
