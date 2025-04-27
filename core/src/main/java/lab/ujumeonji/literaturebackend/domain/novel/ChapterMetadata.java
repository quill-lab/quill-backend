package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "chapter_metadata")
@SQLDelete(sql = "UPDATE chapter_metadata SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public class ChapterMetadata extends BaseEntity<UUID> {

  @Id private UUID id;

  @Column private int likes;

  @Column private int views;

  @Column private int commentCount;

  protected ChapterMetadata() {}

  ChapterMetadata(int likes, int views, int commentCount) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.likes = likes;
    this.views = views;
    this.commentCount = commentCount;
  }

  @Override
  public UUID getId() {
    return id;
  }
}
