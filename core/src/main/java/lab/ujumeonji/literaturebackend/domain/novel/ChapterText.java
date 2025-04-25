package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorId;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chapter_texts")
@SQLDelete(sql = "update chapter_texts set deleted_at = current_timestamp where id = ?")
@Where(clause = "deleted_at IS NULL")
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

    @Column(name = "contributor_id", nullable = false)
    private UUID contributorId;

    @Column
    @Enumerated(EnumType.STRING)
    private ChapterTextStatus status;

    protected ChapterText() {
    }

    ChapterText(Chapter chapter, AccountId accountId, ContributorId contributorId, String content,
                LocalDateTime createdAt, LocalDateTime updatedAt,
                LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.chapter = chapter;
        this.content = content;
        this.accountId = accountId.getId();
        this.contributorId = contributorId.getId();
        this.status = ChapterTextStatus.DRAFT;

        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static ChapterText create(@NotNull Chapter chapter, @NotNull AccountId accountId,
                              @NotNull ContributorId contributorId, @NotNull String content,
                              @NotNull LocalDateTime now) {
        return new ChapterText(chapter, accountId, contributorId, content, now, now, null);
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

    @NotNull
    public ContributorId getContributorId() {
        return ContributorId.from(this.contributorId);
    }

    @NotNull
    public ChapterTextStatus getStatus() {
        return status;
    }
}
