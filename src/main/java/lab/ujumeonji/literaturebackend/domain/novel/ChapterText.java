package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
public class ChapterText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long accountId;

    @Column
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chapter_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chapter"))
    private Chapter chapter;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    protected ChapterText() {
    }

    ChapterText(Chapter chapter, String content, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime deletedAt) {
        this.chapter = chapter;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static ChapterText create(Chapter chapter, String content, LocalDateTime now) {
        return new ChapterText(chapter, content, now, now, null);
    }
}
