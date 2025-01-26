package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;
import java.util.Objects;

@Embeddable
public class NovelTagId implements Serializable {

    private UUID id;

    protected NovelTagId() {}

    public NovelTagId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof NovelTagId novelTagId))
            return false;
        return Objects.equals(id, novelTagId.id);
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
