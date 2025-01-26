package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;
import java.util.Objects;

@Embeddable
public class ChapterTextId implements Serializable {

    private UUID id;

    protected ChapterTextId() {}

    public ChapterTextId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ChapterTextId chapterTextId))
            return false;
        return Objects.equals(id, chapterTextId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
