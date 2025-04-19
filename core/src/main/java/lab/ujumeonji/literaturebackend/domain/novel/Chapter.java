package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.contributor.Contributor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "chapters")
@SQLDelete(sql = "update chapters set deleted_at = current_timestamp where id = ?")
@Where(clause = "deleted_at IS NULL")
public class Chapter extends BaseEntity<UUID> {

    @Id
    private UUID id;

    @Column
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "novel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chapter_novel"))
    private Novel novel;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChapterText> chapterTexts = new ArrayList<>();

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChapterAuthor> chapterAuthors = new ArrayList<>();

    @Column
    private LocalDateTime approvedAt;

    @Column
    @Enumerated(EnumType.STRING)
    private ChapterStatus status;

    @Column
    private Integer chapterNumber;

    protected Chapter() {
    }

    Chapter(String title, String description, Novel novel, Integer chapterNumber,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.title = title;
        this.description = description;
        this.novel = novel;
        this.chapterNumber = chapterNumber;
        this.status = ChapterStatus.DRAFT;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    Chapter(Novel novel, LocalDateTime now) {
        this(null, null, novel, null, now, now, null);
    }

    static Chapter create(@Nonnull String title, @Nonnull String description, @Nonnull Novel novel,
                          int chapterNumber, @Nonnull LocalDateTime now) {
        return new Chapter(title, description, novel, chapterNumber, now, now, null);
    }

    static Chapter createEmpty(@Nonnull Novel novel, @Nonnull LocalDateTime now) {
        return new Chapter(novel, now);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public ChapterId getIdValue() {
        return ChapterId.from(this.id);
    }

    @NotNull
    public Optional<ChapterText> addChapterText(@Nonnull Contributor contributor,
                                                @Nonnull String content,
                                                @Nonnull LocalDateTime now) {
        if (this.status != ChapterStatus.IN_PROGRESS) {
            return Optional.empty();
        }

        ChapterText createdChapterText = ChapterText.create(
                this,
                contributor.getAccountId(),
                contributor.getValueId(),
                content, now);

        this.chapterTexts.add(
                createdChapterText);

        advanceTurn();

        return Optional.of(createdChapterText);
    }

    @Nonnull
    public List<ChapterText> getChapterTexts() {
        return this.chapterTexts;
    }

    @Nullable
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

    Optional<ChapterAuthor> getCurrentChapterAuthor() {
        return this.chapterAuthors.stream()
                .filter(ChapterAuthor::isCurrentWriter)
                .findFirst();
    }

    public void advanceTurn() {
        List<ChapterAuthor> activeAuthors = this.chapterAuthors.stream()
                .toList();

        if (activeAuthors.isEmpty()) {
            return;
        }

        Optional<ChapterAuthor> currentAuthor = getCurrentChapterAuthor();

        if (currentAuthor.isEmpty()) {
            activeAuthors.getFirst().markAsCurrentWriter();
            return;
        }

        currentAuthor.get().unmarkAsCurrentWriter();

        int currentIndex = activeAuthors.indexOf(currentAuthor.get());
        int nextIndex = (currentIndex + 1) % activeAuthors.size();
        activeAuthors.get(nextIndex).markAsCurrentWriter();
    }

    void update(@Nullable String title, @Nonnull LocalDateTime now) {
        if (this.status != ChapterStatus.REQUESTED) {
            return;
        }
        this.title = title;
        setUpdatedAt(now);
    }

    void addChapterAuthor(ChapterAuthor author) {
        this.chapterAuthors.add(author);
    }

    List<ChapterAuthor> getChapterAuthors() {
        return this.chapterAuthors;
    }
}
