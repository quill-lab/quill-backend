package lab.ujumeonji.literaturebackend.domain.contributor;

public enum ContributorRole {
  MAIN("대표 작가"),
  COLLABORATOR("참여 작가");

  private final String alias;

  ContributorRole(String alias) {
    this.alias = alias;
  }

  public String getAlias() {
    return alias;
  }
}
