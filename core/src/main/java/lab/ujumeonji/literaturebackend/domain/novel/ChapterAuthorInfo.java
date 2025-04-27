package lab.ujumeonji.literaturebackend.domain.novel;

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorId;

public record ChapterAuthorInfo(ChapterAuthorId chapterAuthorId, ContributorId contributorId) {

  static ChapterAuthorInfo from(ChapterAuthor chapterAuthor) {
    return new ChapterAuthorInfo(chapterAuthor.getIdValue(), chapterAuthor.getContributorId());
  }
}
