package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "characters")
@SQLDelete(sql = "update characters set deleted_at = current_timestamp where id = ?")
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedFilter", condition = "deleted_at IS NULL")
public class Character extends BaseEntity<UUID> {

  @Id private UUID id;

  @Column(nullable = false)
  private String name;

  @Column(columnDefinition = "text")
  private String description;

  @Column private String profileImage;

  @Column(name = "last_updated_by")
  private UUID lastUpdatedBy;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "novel_id", nullable = false)
  private Novel novel;

  @Column private Integer priority;

  protected Character() {}

  Character(
      String name,
      String description,
      String profileImage,
      @Nullable AccountId lastUpdatedBy,
      Novel novel,
      Integer priority,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      LocalDateTime deletedAt) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.name = name;
    this.description = description;
    this.profileImage = profileImage;
    this.lastUpdatedBy = lastUpdatedBy != null ? lastUpdatedBy.getId() : null;
    this.novel = novel;
    this.priority = priority;
    setCreatedAt(createdAt);
    setUpdatedAt(updatedAt);
    setDeletedAt(deletedAt);

    validate();
  }

  static Character create(
      @NotNull Novel novel,
      @NotNull String name,
      @Nullable String description,
      @NotNull LocalDateTime now) {
    return new Character(name, description, null, null, novel, null, now, now, null);
  }

  private void validate() {}

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  @Nullable
  public String getProfileImage() {
    return profileImage;
  }

  @Nullable
  public AccountId getLastUpdatedBy() {
    return lastUpdatedBy != null ? AccountId.from(lastUpdatedBy) : null;
  }

  @Nullable
  public LocalDateTime getUpdatedAt() {
    if (super.getUpdatedAt().equals(getCreatedAt())) {
      return null;
    }

    return super.getUpdatedAt();
  }

  @Override
  public UUID getId() {
    return id;
  }

  public CharacterId getIdValue() {
    return CharacterId.from(this.id);
  }

  void update(@NotNull String name, @Nullable String description) {
    this.name = name;
    this.description = description;
  }
}
