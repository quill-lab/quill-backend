package lab.ujumeonji.literaturebackend.domain.novel;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lab.ujumeonji.literaturebackend.domain.novel.command.AddCharacterCommand;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "novels")
public class Novel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private String coverImage;

    @Column
    @Enumerated(EnumType.STRING)
    private NovelCategory category;

    @Column(columnDefinition = "text")
    private String synopsis;

    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Character> characters = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chapter> chapters = new ArrayList<>();

    @OneToMany(mappedBy = "novel", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<NovelTag> tags = new ArrayList<>();

    protected Novel() {
    }

    Novel(String title, String description, String coverImage, List<String> tags, String synopsis,
          LocalDateTime createdAt,
          LocalDateTime updatedAt,
          LocalDateTime deletedAt) {
        this.title = title;
        this.description = description;
        this.coverImage = coverImage;
        this.tags = tags.stream()
                .map(tag -> NovelTag.create(tag, this, createdAt))
                .collect(Collectors.toList());
        this.synopsis = synopsis;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    static Novel create(String title, String description, String coverImage, List<String> tags, String synopsis,
                        LocalDateTime now) {
        return new Novel(title, description, coverImage, tags, synopsis, now, now, null);
    }

    void updateBasicInfo(String title, String description, String synopsis, NovelCategory category, LocalDateTime now) {
        this.title = title;
        this.description = description;
        this.synopsis = synopsis;
        this.category = category;
        this.updatedAt = now;
    }

    void updateTags(List<String> tags, LocalDateTime now) {
        this.tags.removeIf(existingTag -> !tags.contains(existingTag.getName()));

        List<String> existingTagNames = this.tags.stream()
                .map(NovelTag::getName)
                .toList();

        tags.stream()
                .filter(tag -> !existingTagNames.contains(tag))
                .forEach(tag -> this.tags.add(NovelTag.create(tag, this, now)));

        this.updatedAt = now;
    }

    Character addCharacter(AddCharacterCommand command, LocalDateTime now) {
        Character character = Character.create(this, command.getName(), command.getDescription(), command.getProfileImage(), command.getPriority(), now);

        this.characters.add(character);

        this.updatedAt = now;

        return character;
    }

    public long getId() {
        return id;
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

    public List<String> getHashtaggedTags() {
        return tags.stream()
                .map(tag -> {
                    String name = tag.getName();
                    return name.startsWith("#") ? name : "#" + name;
                })
                .collect(Collectors.toList());
    }

    public String getDescription() {
        return description;
    }

    @Nullable
    public String getSynopsis() {
        return synopsis;
    }
}
