package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.novel.ChapterTextId;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chapter_texts")
public class ChapterText extends BaseEntity {

    @EmbeddedId
    private ChapterTextId id;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chapter_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chapter_text_chapter"))
    private Chapter chapter;

    @Column(nullable = false)
    private AccountId accountId;

    protected ChapterText() {
    }

    ChapterText(Chapter chapter, String content, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = new ChapterTextId(UuidCreator.getTimeOrderedEpoch());
        this.chapter = chapter;
        this.content = content;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static ChapterText create(@Nonnull Chapter chapter, @Nonnull String content, @Nonnull LocalDateTime now) {
        return new ChapterText(chapter, content, now, now, null);
    }

    @Nonnull
    public ChapterTextId getId() {
        return id;
    }

    @Nonnull
    public String getContent() {
        return content;
    }

    @Nonnull
    public Chapter getChapter() {
        return chapter;
    }
}
