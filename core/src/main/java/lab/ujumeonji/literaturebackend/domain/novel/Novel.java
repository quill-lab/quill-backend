package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.novel.command.AddCharacterCommand;
import lab.ujumeonji.literaturebackend.domain.novel.command.UpdateNovelCommand;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "novels")
@SQLDelete(sql = "update novels set deleted_at = current_timestamp where id = ?")
@Where(clause = "deleted_at IS NULL")
public class Novel extends BaseEntity<UUID> {

    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String coverImage;

    @Column(columnDefinition = "text")
    private String synopsis;

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NovelTag> tags = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Character> characters = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StoryArc> storyArcs = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chapter> chapters = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NovelCategory category;

    protected Novel() {
    }

    Novel(String title, String description, String coverImage, List<String> tags, String synopsis,
          NovelCategory category,
          LocalDateTime createdAt,
          LocalDateTime updatedAt,
          LocalDateTime deletedAt) {
        this.id = UuidCreator.getTimeOrderedEpoch();
        this.title = title;
        this.description = description;
        this.coverImage = coverImage;
        this.synopsis = synopsis;
        this.category = category;

        setUpStoryArcs(createdAt);
        addTags(tags, createdAt);

        setCreatedAt(createdAt);
        setUpdatedAt(updatedAt);
        setDeletedAt(deletedAt);
    }

    static Novel create(String title, String description, NovelCategory category, String coverImage, List<String> tags,
                        String synopsis,
                        LocalDateTime now) {
        return new Novel(title, description, coverImage, tags, synopsis, category, now, now, null);
    }

    public CharacterId addCharacter(@NotNull AddCharacterCommand command, @NotNull LocalDateTime now) {
        Character character = Character.create(this, command.getName(), command.getDescription(), null, null, now);

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
        return tags.stream()
                .map(NovelTag::getName)
                .collect(Collectors.toList());
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public String getSynopsis() {
        return synopsis;
    }

    public void update(@NotNull UpdateNovelCommand command, @NotNull LocalDateTime now) {
        this.title = command.getTitle();
        this.description = command.getDescription();
        this.synopsis = command.getSynopsis();
        this.category = command.getCategory();
        this.addTags(command.getTags(), now);
    }

    public void updatePhase(@NotNull StoryPhase phase, @NotNull String description, @NotNull LocalDateTime now) {
        this.storyArcs.forEach(storyArc -> {
            if (storyArc.getPhase().equals(phase)) {
                storyArc.updatePhase(description, now);
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
        List<String> existingTagNames = this.tags.stream()
                .map(NovelTag::getName)
                .toList();

        List<NovelTag> newTags = Objects.requireNonNull(tagNames)
                .stream()
                .filter(tag -> !existingTagNames.contains(tag))
                .map(tag -> NovelTag.create(tag, this, now))
                .toList();

        this.tags.addAll(newTags);

        this.tags.forEach(tag -> {
            if (!tagNames.contains(tag.getName())) {
                tag.markAsDeleted(now);
            }
        });
    }

    private void setUpStoryArcs(LocalDateTime createdAt) {
        for (StoryPhase phase : StoryPhase.values()) {
            StoryArc storyArc = StoryArc.create(
                    this,
                    phase,
                    createdAt);
            this.storyArcs.add(storyArc);
        }
    }

    @Nonnull
    public Optional<ChapterText> addChapterText(@Nonnull AccountId accountId, @Nonnull ChapterId chapterId,
                                                @Nonnull String content,
                                                @Nonnull LocalDateTime now) {
        return this.chapters.stream()
                .filter(c -> c.getIdValue().equals(chapterId))
                .findFirst()
                .flatMap(chapter -> chapter.addChapterText(
                        accountId,
                        content,
                        now));
    }

    @Nonnull
    public List<ChapterText> findChapterTexts(@Nonnull ChapterId chapterId) {
        return this.chapters.stream()
                .filter(c -> c.getIdValue().equals(chapterId))
                .findFirst()
                .map(Chapter::getChapterTexts)
                .orElse(Collections.emptyList());
    }
}
