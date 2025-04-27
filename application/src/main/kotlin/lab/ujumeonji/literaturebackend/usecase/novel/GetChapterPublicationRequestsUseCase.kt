package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.novel.ChapterId
import lab.ujumeonji.literaturebackend.domain.novel.ChapterPublicationRequestService
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.domain.novel.dto.ChapterPublicationRequestDto
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional(readOnly = true)
class GetChapterPublicationRequestsUseCase(
    private val contributorService: ContributorService,
    private val novelService: NovelService,
    private val chapterPublicationRequestService: ChapterPublicationRequestService
) : UseCase<GetChapterPublicationRequestsUseCase.Request, GetChapterPublicationRequestsUseCase.Response> {
    override fun execute(
        request: Request,
        executedAt: LocalDateTime,
    ): Response {
        val contributorGroup =
            contributorService.findGroupById(ContributorGroupId.from(request.contributorGroupId))
                ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

        if (!contributorGroup.hasManagePermission(AccountId.from(request.accountId))) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE)
        }

        val novel =
            novelService.findNovel(contributorGroup.novelId)
                ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

        val chapterId = ChapterId.from(request.chapterId)
        val publicationRequests = chapterPublicationRequestService.findByChapterId(chapterId)

        return Response(
            requests = publicationRequests.map { ChapterPublicationRequestDto.from(it) }
        )
    }

    data class Request(
        val accountId: String,
        val contributorGroupId: String,
        val chapterId: String,
    )

    data class Response(
        val requests: List<ChapterPublicationRequestDto>,
    )
}