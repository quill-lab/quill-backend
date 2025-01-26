package lab.ujumeonji.literaturebackend.domain.contributor;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public class ContributorRequestId {

    private final UUID id;

    public ContributorRequestId(UUID id) {
        this.id = id;
    }

    @Nonnull
    public static ContributorRequestId from(UUID id) {
        return new ContributorRequestId(id);
    }

    @Nonnull
    public static ContributorRequestId from(String id) {
        return new ContributorRequestId(UUID.fromString(id));
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ContributorRequestId that))
            return false;
        return id.equals(that.id);
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
