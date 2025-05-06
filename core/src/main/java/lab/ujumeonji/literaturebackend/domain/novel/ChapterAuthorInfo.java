package lab.ujumeonji.literaturebackend.domain.novel;

import lab.ujumeonji.literaturebackend.domain.account.AccountId;
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorId;
import org.jetbrains.annotations.NotNull;

public record ChapterAuthorInfo(ChapterAuthorId chapterAuthorId, ContributorId contributorId, AccountId accountId) {

  static ChapterAuthorInfo from(ChapterAuthor chapterAuthor) {
    return new ChapterAuthorInfo(chapterAuthor.getIdValue(), chapterAuthor.getContributorId(), chapterAuthor.getAccountId());
  }
}
