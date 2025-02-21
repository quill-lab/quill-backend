package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "story_arcs")
public class StoryArc extends BaseEntity {

    @Id
    private UUID id;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoryPhase phase;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_chapter_id")
    private Chapter startChapter;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_chapter_id")
    private Chapter endChapter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @OneToMany(mappedBy = "storyArc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chapter> chapters = new ArrayList<>();

    protected StoryArc() {
    }

    StoryArc(String description, Novel novel, StoryPhase phase,
            Chapter startChapter, Chapter endChapter,
            LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.description = description;
        this.novel = novel;
        this.phase = phase;
        this.startChapter = startChapter;
        this.endChapter = endChapter;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static StoryArc create(Novel novel, StoryPhase phase, LocalDateTime now) {
        return new StoryArc(null, novel, phase, null, null, now, now, null);
    }

    public UUID getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public StoryPhase getPhase() {
        return phase;
    }

    @Nullable
    public Integer getLastChapterNumber() {
        if (startChapter != null) {
            return startChapter.getChapterNumber();
        }

        return null;
    }

    @Nullable
    public Integer getFirstChapterNumber() {
        if (endChapter != null) {
            return endChapter.getChapterNumber();
        }

        return null;
    }

    void addChapter(Chapter chapter) {
        this.chapters.add(chapter);
        chapter.setStoryArc(this);
        updateChapterRange();
    }

    void removeChapter(Chapter chapter) {
        this.chapters.remove(chapter);
        chapter.setStoryArc(null);
        updateChapterRange();
    }

    void updatePhase(String description, LocalDateTime now) {
        this.description = description;
        setUpdatedAt(now);
    }

    private void updateChapterRange() {
        if (chapters.isEmpty()) {
            this.startChapter = null;
            this.endChapter = null;
            return;
        }

        this.startChapter = chapters.stream()
                .min(Comparator.comparing(Chapter::getChapterNumber))
                .orElse(null);

        this.endChapter = chapters.stream()
                .max(Comparator.comparing(Chapter::getChapterNumber))
                .orElse(null);
    }

    @Nullable
    public LocalDateTime getLastModifiedAt() {
        if (getCreatedAt().isEqual(getUpdatedAt())) {
            return null;
        }

        return getUpdatedAt();
    }
}
