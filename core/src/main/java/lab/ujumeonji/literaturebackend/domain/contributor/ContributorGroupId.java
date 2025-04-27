package lab.ujumeonji.literaturebackend.domain.contributor;

import jakarta.annotation.Nonnull;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class ContributorGroupId {

  private final UUID id;

  ContributorGroupId(UUID id) {
    this.id = id;
  }

  @Nonnull
  public static ContributorGroupId from(@NotNull final UUID id) {
    return new ContributorGroupId(id);
  }

  @Nonnull
  public static ContributorGroupId from(@Nonnull String id) {
    return new ContributorGroupId(UUID.fromString(id));
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ContributorGroupId contributorGroupId)) return false;
    return id.equals(contributorGroupId.id);
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
