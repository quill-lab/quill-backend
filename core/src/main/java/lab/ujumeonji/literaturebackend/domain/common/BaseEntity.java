package lab.ujumeonji.literaturebackend.domain.common;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.Objects;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;

@MappedSuperclass
public abstract class BaseEntity<ID> implements Persistable<ID> {

  private LocalDateTime createdAt;

  @CreatedDate private LocalDateTime persistedAt;

  @LastModifiedDate private LocalDateTime updatedAt;

  private LocalDateTime deletedAt;

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  protected void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  protected void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public LocalDateTime getDeletedAt() {
    return deletedAt;
  }

  protected void setDeletedAt(LocalDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  @Override
  public boolean isNew() {
    return Objects.isNull(persistedAt);
  }
}
