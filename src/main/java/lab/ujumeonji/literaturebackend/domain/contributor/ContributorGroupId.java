package lab.ujumeonji.literaturebackend.domain.contributor;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;
import java.util.Objects;

@Embeddable
public class ContributorGroupId implements Serializable {

    private UUID id;

    protected ContributorGroupId() {}

    public ContributorGroupId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ContributorGroupId contributorGroupId))
            return false;
        return Objects.equals(id, contributorGroupId.id);
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
