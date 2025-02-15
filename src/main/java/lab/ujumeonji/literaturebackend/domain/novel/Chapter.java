package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.novel.StoryArc;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "chapters")
public class Chapter extends BaseEntity {

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

    @Column
    private LocalDateTime approvedAt;

    @Column
    @Enumerated(EnumType.STRING)
    private ChapterStatus status;

    protected Chapter() {
    }

    Chapter(String title, String description, Novel novel, StoryArc storyArc, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.title = title;
        this.description = description;
        this.novel = novel;
        this.storyArc = storyArc;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static Chapter create(@Nonnull String title, @Nonnull String description, @Nonnull Novel novel, StoryArc storyArc,
            @Nonnull LocalDateTime now) {
        return new Chapter(title, description, novel, storyArc, now, now, null);
    }

    void setStoryArc(StoryArc storyArc) {
        this.storyArc = storyArc;
    }

    public ChapterId getId() {
        return ChapterId.from(this.id);
    }
}
