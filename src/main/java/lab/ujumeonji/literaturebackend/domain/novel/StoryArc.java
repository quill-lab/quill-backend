package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "story_arcs")
public class StoryArc extends BaseEntity {

    @Id
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StoryPhase phase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "novel_id", nullable = false)
    private Novel novel;

    @OneToMany(mappedBy = "storyArc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chapter> chapters = new ArrayList<>();

    protected StoryArc() {
    }

    StoryArc(String title, String description, Novel novel, StoryPhase phase, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.title = title;
        this.description = description;
        this.novel = novel;
        this.phase = phase;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static StoryArc create(String title, String description, Novel novel, StoryPhase phase, LocalDateTime now) {
        return new StoryArc(title, description, novel, phase, now, now, null);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public StoryPhase getPhase() {
        return phase;
    }

    void addChapter(Chapter chapter) {
        this.chapters.add(chapter);
        chapter.setStoryArc(this);
    }

    void removeChapter(Chapter chapter) {
        this.chapters.remove(chapter);
        chapter.setStoryArc(null);
    }
}
