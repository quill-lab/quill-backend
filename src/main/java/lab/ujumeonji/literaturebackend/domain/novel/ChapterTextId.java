package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public class ChapterTextId {

    private final UUID id;

    public ChapterTextId(UUID id) {
        this.id = id;
    }

    @Nonnull
    public static ChapterTextId from(UUID id) {
        return new ChapterTextId(id);
    }

    @Nonnull
    public static ChapterTextId from(String id) {
        return new ChapterTextId(UUID.fromString(id));
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
        return id.equals(chapterTextId.id);
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
