package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;

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

    @Column
    private LocalDateTime approvedAt;

    @Column
    @Enumerated(EnumType.STRING)
    private ChapterStatus status;

    protected Chapter() {
    }

    Chapter(String title, String description, Novel novel, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.title = title;
        this.description = description;
        this.novel = novel;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static Chapter create(@Nonnull String title, @Nonnull String description, @Nonnull Novel novel, @Nonnull LocalDateTime now) {
        return new Chapter(title, description, novel, now, now, null);
    }

    public ChapterId getId() {
        return ChapterId.from(this.id);
    }
}
