package lab.ujumeonji.literaturebackend.domain.post;

import jakarta.annotation.Nonnull;

import java.util.UUID;

public class ContributorGroupRecruitmentId {

    private UUID id;

    public ContributorGroupRecruitmentId(UUID id) {
        this.id = id;
    }

    @Nonnull
    public static ContributorGroupRecruitmentId from(UUID id) {
        return new ContributorGroupRecruitmentId(id);
    }

    @Nonnull
    public static ContributorGroupRecruitmentId from(String id) {
        return new ContributorGroupRecruitmentId(UUID.fromString(id));
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ContributorGroupRecruitmentId that))
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
