package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "chapter_texts")
public class ChapterText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long accountId;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chapter_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chapter_text_chapter"))
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

    static ChapterText create(Chapter chapter, String content, LocalDateTime now) {
        return new ChapterText(chapter, content, now, now, null);
    }
}
