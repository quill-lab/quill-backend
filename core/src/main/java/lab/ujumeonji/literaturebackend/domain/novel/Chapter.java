package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorId;
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorInfo;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "chapters")
@SQLDelete(sql = "update chapters set deleted_at = current_timestamp where id = ?")
@FilterDef(name = "deletedFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedFilter", condition = "deleted_at IS NULL")
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

  @Column private LocalDateTime approvedAt;

  @Column
  @Enumerated(EnumType.STRING)
  private ChapterStatus status;

  @Column private Integer chapterNumber;

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

  Chapter(Novel novel, List<ContributorInfo> contributors, LocalDateTime now) {
    this(null, null, novel, null, contributors, now, now, null);
  }

  static Chapter createEmpty(
      @Nonnull Novel novel,
      @Nonnull List<ContributorInfo> contributors,
      @Nonnull LocalDateTime now) {
    Chapter chapter = new Chapter(novel, contributors, now);

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
    if (this.status != ChapterStatus.REQUESTED) {
      return;
    }
    this.title = title;
    setUpdatedAt(now);
  }

  private Optional<ChapterAuthor> findCurrentAuthor(List<ChapterAuthor> authors) {
    return authors.stream().filter(ChapterAuthor::isCurrentWriter).findFirst();
  }

  boolean isCurrentWriter(ContributorId contributorId) {
    return findCurrentAuthor(this.chapterAuthors)
        .map(current -> current.getContributorId().equals(contributorId))
        .orElse(false);
  }

  public Optional<ChapterAuthorInfo> getCurrentAuthor() {
    return findCurrentAuthor(this.chapterAuthors).map(ChapterAuthorInfo::from);
  }
}
