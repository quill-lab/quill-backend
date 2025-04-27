package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.annotation.Nonnull;
import java.util.UUID;

public class ChapterId {

  private final UUID id;

  public ChapterId(UUID id) {
    this.id = id;
  }

  @Nonnull
  public static ChapterId from(UUID id) {
    return new ChapterId(id);
  }

  @Nonnull
  public static ChapterId from(String id) {
    return new ChapterId(UUID.fromString(id));
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ChapterId chapterId)) return false;
    return id.equals(chapterId.id);
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
