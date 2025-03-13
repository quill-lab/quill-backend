package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "chapters")
public class Chapter extends BaseEntity<UUID> {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "novel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chapter_novel"))
    private Novel novel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_arc_id", foreignKey = @ForeignKey(name = "fk_chapter_story_arc"))
    private StoryArc storyArc;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChapterText> chapterTexts = new ArrayList<>();

    @Column
    private LocalDateTime approvedAt;

    @Column
    @Enumerated(EnumType.STRING)
    private ChapterStatus status;

    @Column
    private Integer chapterNumber;

    protected Chapter() {
    }

    Chapter(String title, String description, Novel novel, StoryArc storyArc, int chapterNumber,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.title = title;
        this.description = description;
        this.novel = novel;
        this.storyArc = storyArc;
        this.chapterNumber = chapterNumber;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static Chapter create(@Nonnull String title, @Nonnull String description, @Nonnull Novel novel,
                          @Nonnull StoryArc storyArc,
                          int chapterNumber, @Nonnull LocalDateTime now) {
        return new Chapter(title, description, novel, storyArc, chapterNumber, now, now, null);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public ChapterId getIdValue() {
        return ChapterId.from(this.id);
    }

    @NotNull
    public Optional<ChapterText> addChapterText(@Nonnull AccountId accountId,
                                                @Nonnull String content,
                                                @Nonnull LocalDateTime now) {
        if (this.status != ChapterStatus.IN_PROGRESS) {
            return Optional.empty();
        }

        ChapterText createdChapterText = ChapterText.create(
                this,
                accountId,
                content, now);

        this.chapterTexts.add(
                createdChapterText);

        return Optional.of(createdChapterText);
    }

    @Nonnull
    public List<ChapterText> getChapterTexts() {
        return this.chapterTexts;
    }

    String getTitle() {
        return title;
    }

    String getDescription() {
        return description;
    }

    Novel getNovel() {
        return novel;
    }

    LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    ChapterStatus getStatus() {
        return status;
    }

    Integer getChapterNumber() {
        return chapterNumber;
    }
}
