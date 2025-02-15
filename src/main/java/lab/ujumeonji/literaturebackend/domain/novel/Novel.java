package lab.ujumeonji.literaturebackend.domain.novel;

import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lab.ujumeonji.literaturebackend.domain.common.BaseEntity;
import lab.ujumeonji.literaturebackend.domain.novel.command.AddCharacterCommand;
import lab.ujumeonji.literaturebackend.domain.novel.command.UpdateNovelCommand;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "novels")
public class Novel extends BaseEntity {

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
    private List<Chapter> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StoryArc> storyArcs = new ArrayList<>();

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

        return character.getId();
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
        this.tags.removeIf(existingTag -> !tagNames.contains(existingTag.getName()));
    }

    public NovelId getId() {
        return NovelId.from(this.id);
    }
}
