package lab.ujumeonji.literaturebackend.domain.novel.command

enum class NovelCategoryEnum(val alias: String) {
    GENERAL("일반"),
    ROMANCE_DRAMA("로맨스/드라마"),
    COMIC("코믹"),
    ESSAY("에세이"),
    FANTASY("판타지"),
    FUSION("퓨전"),
    ACTION_MARTIAL_ARTS("액션/무협"),
    SPORTS_SCHOOL("스포츠/학원"),
    HORROR_MYSTERY("공포/미스터리")

    ;

    fun toNovelCategory(): lab.ujumeonji.literaturebackend.domain.novel.NovelCategory {
        return lab.ujumeonji.literaturebackend.domain.novel.NovelCategory.valueOf(this.name)
    }
}
