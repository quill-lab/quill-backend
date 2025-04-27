package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.annotation.Nonnull;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class CharacterId {

  private final UUID id;

  CharacterId(@NotNull UUID id) {
    this.id = id;
  }

  @Nonnull
  public static CharacterId from(@NotNull UUID id) {
    return new CharacterId(id);
  }

  @Nonnull
  public static CharacterId from(@NotNull String id) {
    return new CharacterId(UUID.fromString(id));
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CharacterId characterId)) return false;
    return id.equals(characterId.id);
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
