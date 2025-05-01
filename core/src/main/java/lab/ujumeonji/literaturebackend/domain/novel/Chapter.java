package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.common.SoftDeleteable;
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorId;
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorInfo;
import org.hibernate.annotations.SQLDelete;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "chapters")
@SQLDelete(sql = "update chapters set deleted_at = current_timestamp where id = ?")
@SoftDeleteable
public class Chapter extends BaseEntity<UUID> {

  @Id private UUID id;

  @Column private String title;

  @Column(columnDefinition = "text")
  private String description;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
      name = "novel_id",
      nullable = false,
      foreignKey = @ForeignKey(name = "fk_chapter_novel"))
  private Novel novel;

  @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ChapterText> chapterTexts = new ArrayList<>();

  @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ChapterAuthor> chapterAuthors = new ArrayList<>();

  @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<ChapterPublicationRequest> publicationRequests = new ArrayList<>();

  @Column private LocalDateTime approvedAt;

  @Column
  @Enumerated(EnumType.STRING)
  private ChapterStatus status;

  @Column(nullable = false)
  private Integer chapterNumber;

  protected Chapter() {}

  Chapter(
      String title,
      String description,
      Novel novel,
      Integer chapterNumber,
      List<ContributorInfo> contributors,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      LocalDateTime deletedAt) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.title = title;
    this.description = description;
    this.novel = novel;
    this.chapterNumber = chapterNumber;
    this.chapterAuthors =
        IntStream.range(0, contributors.size())
            .mapToObj(
                index ->
                    ChapterAuthor.create(
                        this,
                        contributors.get(index).getContributorId(),
                        contributors.get(index).getAccountId(),
                        index == 0,
                        index,
                        createdAt))
            .toList();
    this.status = ChapterStatus.DRAFT;

    setCreatedAt(createdAt);
    setUpdatedAt(updatedAt);
    setDeletedAt(deletedAt);
  }

  Chapter(
      Novel novel, Integer chapterNumber, List<ContributorInfo> contributors, LocalDateTime now) {
    this(null, null, novel, chapterNumber, contributors, now, now, null);
  }

  static Chapter createEmpty(
      @Nonnull Novel novel,
      @Nonnull Integer chapterNumber,
      @Nonnull List<ContributorInfo> contributors,
      @Nonnull LocalDateTime now) {
    Chapter chapter = new Chapter(novel, chapterNumber, contributors, now);

    ContributorInfo contributor = contributors.getFirst();
    chapter.addDefaultText(contributor.getAccountId(), contributor.getContributorId(), now);

    return chapter;
  }

  @Override
  public UUID getId() {
    return id;
  }

  public ChapterId getIdValue() {
    return ChapterId.from(this.id);
  }

  @Nonnull
  public List<ChapterText> getChapterTexts() {
    return Collections.unmodifiableList(this.chapterTexts);
  }

  List<ChapterAuthor> getChapterAuthors() {
    return Collections.unmodifiableList(this.chapterAuthors);
  }

  @Nonnull
  public List<ChapterPublicationRequest> getPublicationRequests() {
    return Collections.unmodifiableList(this.publicationRequests);
  }

  @Nullable
  String getTitle() {
    return title;
  }

  String getDescription() {
    return description;
  }

  LocalDateTime getApprovedAt() {
    return approvedAt;
  }

  ChapterStatus getStatus() {
    return status;
  }

  Integer getChapterNumber() {
    return chapterNumber;
  }

  void addDefaultText(
      @Nonnull AccountId accountId,
      @Nonnull ContributorId contributorId,
      @Nonnull LocalDateTime now) {
    ChapterText createdChapterText = ChapterText.create(this, accountId, contributorId, "", now);

    this.chapterTexts.add(createdChapterText);
  }

  void advanceTurn(LocalDateTime now) {
    List<ChapterAuthor> activeAuthors =
        this.chapterAuthors.stream()
            .sorted(Comparator.comparingInt(ChapterAuthor::getWritingOrder))
            .toList();

    if (activeAuthors.isEmpty()) {
      return;
    }

    Optional<ChapterAuthor> currentAuthorOpt = findCurrentAuthor(activeAuthors);

    int currentIndex = currentAuthorOpt.map(activeAuthors::indexOf).orElse(-1);

    currentAuthorOpt.ifPresent(ChapterAuthor::unmarkAsCurrentWriter);

    int nextIndex = (currentIndex + 1) % activeAuthors.size();
    ChapterAuthor nextAuthor = activeAuthors.get(nextIndex);

    nextAuthor.markAsCurrentWriter();

    addDefaultText(nextAuthor.getAccountId(), nextAuthor.getContributorId(), now);
  }

  void update(@Nullable String title, @Nonnull LocalDateTime now) {
    if (this.status == ChapterStatus.REQUESTED || this.status == ChapterStatus.APPROVED) {
      return;
    }
    this.title = title;
    setUpdatedAt(now);
  }

  boolean requestPublication(@Nonnull AccountId requesterId, @Nonnull LocalDateTime now) {
    if (this.status != ChapterStatus.DRAFT && this.status != ChapterStatus.IN_PROGRESS) {
      return false;
    }

    ChapterPublicationRequest request = ChapterPublicationRequest.create(this, requesterId, now);
    this.publicationRequests.add(request);

    this.status = ChapterStatus.REQUESTED;
    setUpdatedAt(now);
    return true;
  }

  boolean approvePublication(@Nonnull AccountId reviewerId, @Nonnull LocalDateTime now) {
    if (this.status != ChapterStatus.REQUESTED) {
      return false;
    }

    Optional<ChapterPublicationRequest> latestRequest =
        this.publicationRequests.stream()
            .filter(request -> request.getStatus() == ChapterPublicationRequestStatus.REQUESTED)
            .max(Comparator.comparing(BaseEntity::getCreatedAt));

    if (latestRequest.isEmpty()) {
      return false;
    }

    boolean approved = latestRequest.get().approve(reviewerId, now);
    if (!approved) {
      return false;
    }

    this.status = ChapterStatus.APPROVED;
    this.approvedAt = now;
    setUpdatedAt(now);
    return true;
  }

  boolean rejectPublication(@Nonnull AccountId reviewerId, @Nonnull LocalDateTime now) {
    if (this.status != ChapterStatus.REQUESTED) {
      return false;
    }

    Optional<ChapterPublicationRequest> latestRequest =
        this.publicationRequests.stream()
            .filter(request -> request.getStatus() == ChapterPublicationRequestStatus.REQUESTED)
            .max(Comparator.comparing(BaseEntity::getCreatedAt));

    if (latestRequest.isEmpty()) {
      return false;
    }

    boolean rejected = latestRequest.get().reject(reviewerId, now);
    if (!rejected) {
      return false;
    }

    this.status = ChapterStatus.REJECTED;
    setUpdatedAt(now);
    return true;
  }

  private Optional<ChapterAuthor> findCurrentAuthor(List<ChapterAuthor> authors) {
    return authors.stream().filter(ChapterAuthor::isCurrentWriter).findFirst();
  }

  boolean isCurrentWriter(ContributorId contributorId) {
    return findCurrentAuthor(this.chapterAuthors)
        .map(current -> current.getContributorId().equals(contributorId))
        .orElse(false);
  }

  Optional<ChapterAuthorInfo> getCurrentAuthor() {
    return findCurrentAuthor(this.chapterAuthors).map(ChapterAuthorInfo::from);
  }
}
