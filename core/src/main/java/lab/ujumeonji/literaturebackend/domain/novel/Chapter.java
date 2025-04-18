package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// Import for the new relationship
import lab.ujumeonji.literaturebackend.domain.contributor.ChapterContributor;
import lab.ujumeonji.literaturebackend.domain.novel.ChapterAuthor;
import lab.ujumeonji.literaturebackend.domain.contributor.Contributor;

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

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contributor> contributors = new ArrayList<>();

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
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

    public List<Contributor> getOrderedContributors() {
        return this.contributors.stream()
                .filter(c -> !c.isDeleted())
                .sorted(Comparator.comparingInt(Contributor::getWritingOrder))
                .toList();
    }

    @Nullable
    public Contributor getCurrentWriter() {
        return this.contributors.stream()
                .filter(c -> !c.isDeleted() && c.isCurrentWriter())
                .findFirst()
                .orElse(null);
    }

    void addContributor(Contributor contributor) {
        this.contributors.add(contributor);
    }

    public List<ChapterAuthor> getOrderedChapterAuthors() {
        return this.chapterAuthors.stream()
                .filter(ca -> ca.getContributor() != null && !ca.getContributor().isDeleted())
                .sorted(Comparator.comparingInt(ca -> ca.getContributor().getWritingOrder()))
                .toList();
    }

    @Nullable
    public ChapterAuthor getCurrentChapterAuthor() {
        return this.chapterAuthors.stream()
                .filter(ChapterAuthor::isCurrentWriter)
                .findFirst()
                .orElse(null);
    }

    void addChapterAuthor(ChapterAuthor chapterAuthor) {
        this.chapterAuthors.add(chapterAuthor);
    }

    public void advanceTurn() {
        List<ChapterAuthor> orderedActiveLinks = getOrderedChapterAuthors();
        if (orderedActiveLinks.isEmpty()) {
            return;
        }

        ChapterAuthor currentLink = getCurrentChapterAuthor();

        if (currentLink == null) {
            orderedActiveLinks.get(0).setCurrentWriter(true);
            return;
        }

        currentLink.setCurrentWriter(false);

        int currentIndex = orderedActiveLinks.indexOf(currentLink);
        if (currentIndex == -1) {
            orderedActiveLinks.get(0).setCurrentWriter(true);
            return;
        }

        int nextIndex = (currentIndex + 1) % orderedActiveLinks.size();
        orderedActiveLinks.get(nextIndex).setCurrentWriter(true);
    }

    void update(@Nullable String title, @Nonnull LocalDateTime now) {
        if (this.status != ChapterStatus.REQUESTED) {
            return;
        }
        this.title = title;
        setUpdatedAt(now);
    }
}
