package lab.ujumeonji.literaturebackend.domain.novel;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class NovelId {

    private final UUID id;

    public NovelId(UUID id) {
        this.id = id;
    }

    @NotNull
    public static NovelId from(UUID id) {
        return new NovelId(id);
    }

    @NotNull
    public static NovelId from(String id) {
        return new NovelId(UUID.fromString(id));
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof NovelId novelId))
            return false;
        return id.equals(novelId.id);
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
