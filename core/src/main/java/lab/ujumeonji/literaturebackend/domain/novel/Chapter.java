package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorId;
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorInfo;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

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

    Chapter(String title, String description, Novel novel, Integer chapterNumber, List<ContributorInfo> contributors,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.title = title;
        this.description = description;
        this.novel = novel;
        this.chapterNumber = chapterNumber;
        this.chapterAuthors = IntStream.range(0, contributors.size())
                .mapToObj(index -> ChapterAuthor.create(
                        this,
                        contributors.get(index).getContributorId(),
                        contributors.get(index).getAccountId(),
                        index == 0,
                        createdAt))
                .toList();
        this.status = ChapterStatus.DRAFT;

        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    Chapter(Novel novel, List<ContributorInfo> contributors, LocalDateTime now) {
        this(null, null, novel, null, contributors, now, now, null);
    }

    static Chapter createEmpty(@Nonnull Novel novel, @Nonnull List<ContributorInfo> contributors,
                               @Nonnull LocalDateTime now) {
        return new Chapter(novel, contributors, now);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public ChapterId getIdValue() {
        return ChapterId.from(this.id);
    }

    @Nonnull
    public List<ChapterText> getChapterTexts() {
        return Collections.unmodifiableList(this.chapterTexts);
    }

    List<ChapterAuthor> getChapterAuthors() {
        return Collections.unmodifiableList(this.chapterAuthors);
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

    @Nonnull
    public Optional<ChapterText> addChapterText(@Nonnull ContributorInfo contributorInfo,
                                                @Nonnull String content,
                                                @Nonnull LocalDateTime now) {
        if (this.status != ChapterStatus.IN_PROGRESS) {
            return Optional.empty();
        }

        if (!isCurrentWriter(contributorInfo.getContributorId())) {
            return Optional.empty();
        }

        ChapterText createdChapterText = ChapterText.create(
                this,
                contributorInfo.getAccountId(),
                contributorInfo.getContributorId(),
                content, now);

        this.chapterTexts.add(createdChapterText);

        advanceTurn(now);

        return Optional.of(createdChapterText);
    }

    void advanceTurn(LocalDateTime now) {
        List<ChapterAuthor> activeAuthors = this.chapterAuthors.stream()
                .toList();

        if (activeAuthors.isEmpty()) {
            return;
        }

        Optional<ChapterAuthor> currentAuthorOpt = findCurrentAuthor(activeAuthors);

        int currentIndex = currentAuthorOpt
                .map(activeAuthors::indexOf)
                .orElse(-1);

        currentAuthorOpt.ifPresent(ChapterAuthor::unmarkAsCurrentWriter);

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

    private Optional<ChapterAuthor> findCurrentAuthor(List<ChapterAuthor> authors) {
        return authors.stream()
                .filter(ChapterAuthor::isCurrentWriter)
                .findFirst();
    }

    private boolean isCurrentWriter(ContributorId contributorId) {
        return findCurrentAuthor(this.chapterAuthors)
                .map(current -> current.getContributorId().equals(contributorId))
                .orElse(false);
    }
}
