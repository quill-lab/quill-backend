package lab.ujumeonji.literaturebackend.domain.account;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;
import java.util.Objects;

@Embeddable
public class AccountId implements Serializable {

    private UUID id;

    protected AccountId() {}

    public AccountId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AccountId accountId))
            return false;
        return Objects.equals(id, accountId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
