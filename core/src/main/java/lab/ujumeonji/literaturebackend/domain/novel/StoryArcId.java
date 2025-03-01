package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public class StoryArcId {

    private final UUID id;

    public StoryArcId(UUID id) {
        this.id = id;
    }

    @Nonnull
    public static StoryArcId from(UUID id) {
        return new StoryArcId(id);
    }

    @Nonnull
    public static StoryArcId from(String id) {
        return new StoryArcId(UUID.fromString(id));
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof StoryArcId novelTagId))
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
