package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chapter_texts")
public class ChapterText extends BaseEntity<UUID> {

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

    ChapterText(Chapter chapter, AccountId accountId, String content, LocalDateTime createdAt, LocalDateTime updatedAt,
                LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.chapter = chapter;
        this.content = content;
        this.accountId = accountId.getId();
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static ChapterText create(@NotNull Chapter chapter, @NotNull AccountId accountId, @NotNull String content,
                              @NotNull LocalDateTime now) {
        return new ChapterText(chapter, accountId, content, now, now, null);
    }

    @Override
    public UUID getId() {
        return id;
    }

    @NotNull
    public ChapterTextId getIdValue() {
        return ChapterTextId.from(this.id);
    }

    @NotNull
    public String getContent() {
        return content;
    }

    @NotNull
    public AccountId getAccountId() {
        return AccountId.from(this.accountId);
    }
}
