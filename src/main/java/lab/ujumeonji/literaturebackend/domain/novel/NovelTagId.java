package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public class NovelTagId {

    private final UUID id;

    public NovelTagId(UUID id) {
        this.id = id;
    }

    @Nonnull
    public static NovelTagId from(UUID id) {
        return new NovelTagId(id);
    }

    @Nonnull
    public static NovelTagId from(String id) {
        return new NovelTagId(UUID.fromString(id));
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
        return id.equals(novelTagId.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
