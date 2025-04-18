package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChapterAuthorId implements Serializable {
    private UUID id;

    private ChapterAuthorId(@NotNull UUID id) {
        this.id = id;
    }

    public static ChapterAuthorId from(@NotNull UUID id) {
        return new ChapterAuthorId(id);
    }
}