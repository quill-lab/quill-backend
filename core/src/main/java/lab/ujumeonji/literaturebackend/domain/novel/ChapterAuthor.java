package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntityWithoutUpdate;
import lab.ujumeonji.literaturebackend.domain.contributor.Contributor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chapter_authors")
public class ChapterAuthor extends BaseEntityWithoutUpdate<UUID> {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chapter_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chapter_author_chapter"))
    private Chapter chapter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "contributor_id", nullable = false, foreignKey = @ForeignKey(name = "fk_chapter_author_contributor"))
    private Contributor contributor;

    @Column(nullable = false)
    private boolean isCurrentWriter;

    protected ChapterAuthor() {
    }

    ChapterAuthor(
            @NotNull Chapter chapter,
            @NotNull Contributor contributor,
            boolean isCurrentWriter,
            @NotNull LocalDateTime createdAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.chapter = chapter;
        this.contributor = contributor;
        this.isCurrentWriter = isCurrentWriter;
        setCreatedAt(createdAt);
    }

    public static ChapterAuthor create(
            @NotNull Chapter chapter,
            @NotNull Contributor contributor,
            boolean isCurrentWriter,
            @NotNull LocalDateTime now) {
        return new ChapterAuthor(chapter, contributor, isCurrentWriter, now);
    }

    @Override
    public UUID getId() {
        return id;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public Contributor getContributor() {
        return contributor;
    }

    public boolean isCurrentWriter() {
        return isCurrentWriter;
    }

    void setCurrentWriter(boolean currentWriter) {
        isCurrentWriter = currentWriter;
    }
}