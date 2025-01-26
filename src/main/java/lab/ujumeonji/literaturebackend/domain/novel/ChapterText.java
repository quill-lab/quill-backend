package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import com.github.f4b6a3.uuid.UuidCreator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chapter_texts")
public class ChapterText extends BaseEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private Long accountId;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chapter_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chapter_text_chapter"))
    private Chapter chapter;

    protected ChapterText() {
    }

    ChapterText(Chapter chapter, String content, LocalDateTime createdAt, LocalDateTime updatedAt,
            LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.chapter = chapter;
        this.content = content;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static ChapterText create(Chapter chapter, String content, LocalDateTime now) {
        return new ChapterText(chapter, content, now, now, null);
    }

    public UUID getId() {
        return id;
    }
}
