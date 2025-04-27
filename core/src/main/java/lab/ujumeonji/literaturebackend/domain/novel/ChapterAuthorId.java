package lab.ujumeonji.literaturebackend.domain.novel;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class ChapterAuthorId {

  private final UUID id;

  public ChapterAuthorId(UUID id) {
    this.id = id;
  }

  @NotNull
  public static ChapterAuthorId from(UUID id) {
    return new ChapterAuthorId(id);
  }

  @NotNull
  public static ChapterAuthorId from(String id) {
    return new ChapterAuthorId(UUID.fromString(id));
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ChapterAuthorId chapterAuthorId)) return false;
    return id.equals(chapterAuthorId.id);
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
