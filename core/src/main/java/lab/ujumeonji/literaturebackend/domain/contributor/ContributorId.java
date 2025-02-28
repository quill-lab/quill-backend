package lab.ujumeonji.literaturebackend.domain.contributor;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public class ContributorId {

    private UUID id;

    public ContributorId(UUID id) {
        this.id = id;
    }

    @Nonnull
    public static ContributorId from(UUID id) {
        return new ContributorId(id);
    }

    @Nonnull
    public static ContributorId from(String id) {
        return new ContributorId(UUID.fromString(id));
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ContributorId that))
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
