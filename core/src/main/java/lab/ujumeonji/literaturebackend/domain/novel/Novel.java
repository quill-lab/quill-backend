package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorInfo;
import lab.ujumeonji.literaturebackend.domain.novel.command.AddCharacterCommand;
import lab.ujumeonji.literaturebackend.domain.novel.command.UpdateNovelCommand;
import lab.ujumeonji.literaturebackend.domain.novel.command.UpsertCharactersCommand;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.jetbrains.annotations.Nullable;

@Entity
@Table(name = "novels")
@SQLDelete(sql = "update novels set deleted_at = current_timestamp where id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Novel extends BaseEntity<UUID> {

  @Id private UUID id;

  @Column(nullable = false)
  private String title;

  @Column private String coverImage;

  @Column(columnDefinition = "text")
  private String synopsis;

  @OneToMany(
      mappedBy = "novel",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private List<NovelTag> tags = new ArrayList<>();

  @OneToMany(
      mappedBy = "novel",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  private List<Character> characters = new ArrayList<>();

  @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<StoryArc> storyArcs = new ArrayList<>();

  @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Chapter> chapters = new ArrayList<>();

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private NovelCategory category;

  protected Novel() {}

  Novel(
      String title,
      String coverImage,
      List<String> tags,
      String synopsis,
      NovelCategory category,
      LocalDateTime createdAt,
      LocalDateTime updatedAt,
      LocalDateTime deletedAt) {
    this.id = UuidCreator.getTimeOrderedEpoch();
    this.title = title;
    this.coverImage = coverImage;
    this.synopsis = synopsis;
    this.category = category;

    setUpStoryArcs(createdAt);
    addTags(tags, createdAt);

    setCreatedAt(createdAt);
    setUpdatedAt(updatedAt);
    setDeletedAt(deletedAt);
  }

  static Novel create(
      String title,
      NovelCategory category,
      String coverImage,
      List<String> tags,
      String synopsis,
      LocalDateTime now) {
    return new Novel(title, coverImage, tags, synopsis, category, now, now, null);
  }

  public CharacterId addCharacter(
      @NotNull AddCharacterCommand command, @NotNull LocalDateTime now) {
    Character character = Character.create(this, command.getName(), command.getDescription(), now);

    this.characters.add(character);

    return character.getIdValue();
  }

  public List<Character> getCharacters() {
    return characters;
  }

  public NovelCategory getCategory() {
    return category;
  }

  public String getTitle() {
    return title;
  }

  public List<String> getTagNames() {
    return tags.stream().map(NovelTag::getName).collect(Collectors.toList());
  }

  @Nullable
  public String getSynopsis() {
    return synopsis;
  }

  public void update(@NotNull UpdateNovelCommand command, @NotNull LocalDateTime now) {
    this.title = command.getTitle();
    this.synopsis = command.getSynopsis();
    this.category = command.getCategory();
    this.addTags(command.getTags(), now);
  }

  public void updatePhase(
      @NotNull StoryPhase phase,
      @Nullable Integer startChapterNumber,
      @Nullable Integer endChapterNumber,
      @Nullable String description,
      @NotNull LocalDateTime now) {
    this.storyArcs.forEach(
        storyArc -> {
          if (storyArc.getPhase().equals(phase)) {
            storyArc.updatePhase(startChapterNumber, endChapterNumber, description, now);
          }
        });
  }

  @Override
  public UUID getId() {
    return id;
  }

  public NovelId getIdValue() {
    return NovelId.from(this.id);
  }

  public List<StoryArc> getStoryArcs() {
    return storyArcs.stream()
        .sorted(Comparator.comparing(storyArc -> storyArc.getPhase().ordinal()))
        .collect(Collectors.toList());
  }

  private void addTags(@NotNull List<String> tagNames, @NotNull LocalDateTime now) {
    List<String> existingTagNames = this.tags.stream().map(NovelTag::getName).toList();

    List<NovelTag> newTags =
        Objects.requireNonNull(tagNames).stream()
            .filter(tag -> !existingTagNames.contains(tag))
            .map(tag -> NovelTag.create(tag, this, now))
            .toList();

    this.tags.addAll(newTags);

    this.tags.forEach(
        tag -> {
          if (!tagNames.contains(tag.getName())) {
            tag.markAsDeleted(now);
          }
        });
  }

  private void setUpStoryArcs(LocalDateTime createdAt) {
    for (StoryPhase phase : StoryPhase.values()) {
      StoryArc storyArc = StoryArc.create(this, phase, createdAt);
      this.storyArcs.add(storyArc);
    }
  }

  @Nonnull
  public List<ChapterText> findChapterTexts(@Nonnull ChapterId chapterId) {
    return this.chapters.stream()
        .filter(c -> c.getIdValue().equals(chapterId))
        .findFirst()
        .map(Chapter::getChapterTexts)
        .orElse(Collections.emptyList());
  }

  public Chapter createChapter(
      @Nonnull List<ContributorInfo> orderedContributorIds,
      @Nullable String title,
      @Nullable String description,
      @Nonnull LocalDateTime now) {
    Chapter chapter =
        Chapter.createEmpty(
            this, this.chapters.size() + 1, orderedContributorIds, title, description, now);

    this.chapters.add(chapter);

    return chapter;
  }

  public List<Character> replaceCharacters(
      @Nonnull List<UpsertCharactersCommand> commands,
      @Nonnull AccountId updatedBy,
      @Nonnull LocalDateTime now) {
    // Remove existing characters
    this.characters.clear();

    // Add new characters
    List<Character> newCharacters =
        commands.stream()
            .map(
                command -> Character.create(this, command.getName(), command.getDescription(), now))
            .collect(Collectors.toList());
    this.characters.addAll(newCharacters);
    return newCharacters;
  }

  public boolean updateChapter(
      @Nonnull ChapterId chapterId,
      @Nullable String title,
      @Nullable String description,
      @Nonnull LocalDateTime now) {
    Optional<Chapter> chapter =
        this.chapters.stream().filter(c -> c.getIdValue().equals(chapterId)).findFirst();

    if (chapter.isPresent()) {
      chapter.get().update(title, description, now);
      return true;
    }

    return false;
  }

  public boolean updateChapter(
      @Nonnull ChapterId chapterId, @Nullable String title, @Nonnull LocalDateTime now) {
    return updateChapter(chapterId, title, null, now);
  }

  @Nonnull
  public Optional<ChapterText> findDraftChapterText(@Nonnull ChapterId chapterId) {
    return this.chapters.stream()
        .filter(chapter -> chapter.getIdValue().equals(chapterId))
        .findFirst()
        .flatMap(
            chapter ->
                chapter.getChapterTexts().stream()
                    .filter(text -> text.getStatus() == ChapterTextStatus.DRAFT)
                    .findFirst());
  }

  public boolean updateDraftChapterText(
      @Nonnull ChapterId chapterId,
      @Nonnull ContributorInfo contributor,
      @Nonnull String content,
      @Nonnull LocalDateTime now) {
    Optional<ChapterText> draftTextOpt = findDraftChapterText(chapterId);

    if (draftTextOpt.isEmpty()) {
      return false;
    }

    ChapterText draftText = draftTextOpt.get();

    if (!draftText.getContributorId().equals(contributor.getContributorId())) {
      return false;
    }

    return draftText.updateContent(contributor.getContributorId(), content, now);
  }

  public boolean finalizeChapterText(
      @Nonnull ChapterId chapterId,
      @Nonnull ContributorInfo contributorInfo,
      @Nonnull LocalDateTime now) {
    Optional<Chapter> chapterOpt =
        this.chapters.stream().filter(c -> c.getIdValue().equals(chapterId)).findFirst();

    if (chapterOpt.isEmpty()) {
      return false;
    }

    Chapter chapter = chapterOpt.get();

    if (!chapter.isCurrentWriter(contributorInfo.getContributorId())) {
      return false;
    }

    Optional<ChapterText> draftTextOpt = findDraftChapterText(chapterId);

    if (draftTextOpt.isEmpty()) {
      return false;
    }

    ChapterText draftText = draftTextOpt.get();

    if (!draftText.getContributorId().equals(contributorInfo.getContributorId())) {
      return false;
    }

    boolean finalized = draftText.finalize(now);

    if (!finalized) {
      return false;
    }

    chapter.advanceTurn(now);

    return true;
  }

  public boolean requestChapterPublication(
      @Nonnull ChapterId chapterId, @Nonnull AccountId requesterId, @Nonnull LocalDateTime now) {
    Optional<Chapter> chapterOpt =
        this.chapters.stream().filter(c -> c.getIdValue().equals(chapterId)).findFirst();

    if (chapterOpt.isEmpty()) {
      return false;
    }

    Chapter chapter = chapterOpt.get();
    return chapter.requestPublication(requesterId, now);
  }

  public boolean approveChapterPublication(
      @Nonnull ChapterId chapterId, @Nonnull AccountId reviewerId, @Nonnull LocalDateTime now) {
    Optional<Chapter> chapterOpt =
        this.chapters.stream().filter(c -> c.getIdValue().equals(chapterId)).findFirst();

    if (chapterOpt.isEmpty()) {
      return false;
    }

    Chapter chapter = chapterOpt.get();
    return chapter.approvePublication(reviewerId, now);
  }

  /**
   * Reject publication of a chapter. Only chapters in REQUESTED status can be rejected.
   *
   * @param chapterId ID of the chapter to reject
   * @param reviewerId ID of the reviewer who is rejecting the publication
   * @param now Current timestamp
   * @return true if the rejection was successful, false otherwise
   */
  public boolean rejectChapterPublication(
      @Nonnull ChapterId chapterId, @Nonnull AccountId reviewerId, @Nonnull LocalDateTime now) {
    Optional<Chapter> chapterOpt =
        this.chapters.stream().filter(c -> c.getIdValue().equals(chapterId)).findFirst();

    if (chapterOpt.isEmpty()) {
      return false;
    }

    Chapter chapter = chapterOpt.get();
    return chapter.rejectPublication(reviewerId, now);
  }

  /**
   * Get the publication requests for a specific chapter.
   *
   * @param chapterId ID of the chapter
   * @return List of publication requests for the chapter, or empty list if chapter not found
   */
  @Nonnull
  public List<ChapterPublicationRequest> getChapterPublicationRequests(
      @Nonnull ChapterId chapterId) {
    return this.chapters.stream()
        .filter(c -> c.getIdValue().equals(chapterId))
        .findFirst()
        .map(Chapter::getPublicationRequests)
        .orElse(Collections.emptyList());
  }
}
