package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.annotation.Nonnull;
import java.util.UUID;

public class ChapterPublicationRequestId {

  private final UUID id;

  public ChapterPublicationRequestId(UUID id) {
    this.id = id;
  }

  @Nonnull
  public static ChapterPublicationRequestId from(UUID id) {
    return new ChapterPublicationRequestId(id);
  }

  @Nonnull
  public static ChapterPublicationRequestId from(String id) {
    return new ChapterPublicationRequestId(UUID.fromString(id));
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ChapterPublicationRequestId chapterPublicationRequestId)) return false;
    return id.equals(chapterPublicationRequestId.id);
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
