package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chapter_texts")
public class ChapterText extends BaseEntity {

    @Id
    private UUID id;

    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chapter_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chapter_text_chapter"))
    private Chapter chapter;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    protected ChapterText() {
    }

    ChapterText(@Nonnull Chapter chapter, @Nonnull AccountId accountId, @Nonnull String content, @Nonnull LocalDateTime createdAt, @Nonnull LocalDateTime updatedAt,
                LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.chapter = chapter;
        this.content = content;
        this.accountId = accountId.getId();
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static ChapterText create(@Nonnull Chapter chapter, @Nonnull AccountId accountId, @Nonnull String content, @Nonnull LocalDateTime now) {
        return new ChapterText(chapter, accountId, content, now, now, null);
    }

    @Nonnull
    public ChapterTextId getId() {
        return ChapterTextId.from(this.id);
    }
}
