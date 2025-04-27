package lab.ujumeonji.literaturebackend.domain.account;

import jakarta.annotation.Nonnull;
import java.util.UUID;

public class AccountId {

  private final UUID id;

  AccountId(UUID id) {
    this.id = id;
  }

  @Nonnull
  public static AccountId from(UUID id) {
    return new AccountId(id);
  }

  @Nonnull
  public static AccountId from(String id) {
    return new AccountId(UUID.fromString(id));
  }

  public UUID getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AccountId accountId)) return false;
    return id.equals(accountId.id);
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
