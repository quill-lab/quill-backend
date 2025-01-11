package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "novels")
public class Novel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String coverImage;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Character> characters = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chapter> chapters = new ArrayList<>();

    protected Novel() {
    }

    Novel(String title, String description, String coverImage, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime deletedAt) {
        this.title = title;
        this.description = description;
        this.coverImage = coverImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Novel create(String title, String description, String coverImage, LocalDateTime now) {
        return new Novel(title, description, coverImage, now, now, null);
    }
}
