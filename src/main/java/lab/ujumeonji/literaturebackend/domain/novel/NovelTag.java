package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.novel.NovelTagId;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "novel_tags")
public class NovelTag extends BaseEntity {

    @EmbeddedId
    private NovelTagId id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "novel_id", nullable = false, foreignKey = @ForeignKey(name = "fk_novel_tag_novel"))
    private Novel novel;

    protected NovelTag() {
    }

    NovelTag(String name, Novel novel, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt) {
        this.id = new NovelTagId(UuidCreator.getTimeOrderedEpoch());
        this.name = name;
        this.novel = novel;
        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static NovelTag create(String name, Novel novel, LocalDateTime now) {
        return new NovelTag(name, novel, now, now, null);
    }

    public String getName() {
        return name;
    }

    public NovelTagId getId() {
        return id;
    }

    public Novel getNovel() {
        return novel;
    }
}
