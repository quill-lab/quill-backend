package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "story_arcs")
@SQLDelete(sql = "update story_arcs set deleted_at = current_timestamp where id = ?")
@SQLRestriction("deleted_at IS NULL")
public class StoryArc extends BaseEntity<UUID> {

  @Id private UUID id;

  @Column(columnDefinition = "text")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StoryPhase phase;

  @Column private Integer startChapterNumber;

  @Column private Integer endChapterNumber;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "novel_id", nullable = false)
  private Novel novel;

  protected StoryArc() {}

  StoryArc(
      String description,
      Novel novel,
      StoryPhase phase,
      Integer startChapterNumber,
      Integer endChapterNumber,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      LocalDateTime deletedAt) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.description = description;
    this.novel = novel;
    this.phase = phase;
    this.startChapterNumber = startChapterNumber;
    this.endChapterNumber = endChapterNumber;
    setCreatedAt(createdAt);
    setUpdatedAt(updatedAt);
    setDeletedAt(deletedAt);
  }

  static StoryArc create(Novel novel, StoryPhase phase, LocalDateTime now) {
    return new StoryArc(null, novel, phase, null, null, now, now, null);
  }

  @Override
  public UUID getId() {
    return id;
  }

  public StoryArcId getIdValue() {
    return StoryArcId.from(id);
  }

  public String getDescription() {
    return description;
  }

  public StoryPhase getPhase() {
    return phase;
  }

  @Nullable
  public Integer getStartChapterNumber() {
    return startChapterNumber;
  }

  @Nullable
  public Integer getEndChapterNumber() {
    return endChapterNumber;
  }

  void updatePhase(
      Integer startChapterNumber, Integer endChapterNumber, String description, LocalDateTime now) {
    this.startChapterNumber = startChapterNumber;
    this.endChapterNumber = endChapterNumber;
    this.description = description;
    setUpdatedAt(now);
  }

  @Nullable
  public LocalDateTime getLastModifiedAt() {
    if (getCreatedAt().isEqual(getUpdatedAt())) {
      return null;
    }

    return getUpdatedAt();
  }
}
