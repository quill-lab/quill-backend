package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chapters")
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "novel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_novel"))
    private Novel novel;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChapterText> chapterTexts = new ArrayList<>();

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    protected Chapter() {
    }

    Chapter(String title, String description, Novel novel, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime deletedAt) {
        this.title = title;
        this.description = description;
        this.novel = novel;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    static Chapter create(String title, String description, Novel novel, LocalDateTime now) {
        return new Chapter(title, description, novel, now, now, null);
    }
}
