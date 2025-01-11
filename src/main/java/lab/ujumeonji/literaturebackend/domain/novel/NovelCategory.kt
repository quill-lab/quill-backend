package lab.ujumeonji.literaturebackend.domain.novel

enum class NovelCategory(
    val alias: String,
) {
    GENERAL("일반"),
    ROMANCE_DRAMA("로맨스/드라마"),
    COMIC("코믹"),
    ESSAY("에세이"),
    FANTASY("판타지"),
    FUSION("퓨전"),
    ACTION_MARTIAL_ARTS("액션/무협"),
    SPORTS_SCHOOL("스포츠/학원"),
    HORROR_MYSTERY("공포/미스터리")
}
