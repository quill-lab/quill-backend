package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "chapter_metadata")
public class ChapterMetadata extends BaseEntity {

    @Id
    private UUID id;

    @Column
    private int likes;

    @Column
    private int views;

    @Column
    private int commentCount;

    protected ChapterMetadata() {
    }

    ChapterMetadata(int likes, int views, int commentCount) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.likes = likes;
        this.views = views;
        this.commentCount = commentCount;
    }
}
