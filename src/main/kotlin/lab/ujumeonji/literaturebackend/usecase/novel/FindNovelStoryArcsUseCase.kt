package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.account.AccountId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroup
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupId
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.novel.Novel
import lab.ujumeonji.literaturebackend.domain.novel.NovelId
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.domain.novel.StoryPhase
import lab.ujumeonji.literaturebackend.support.exception.BusinessException
import lab.ujumeonji.literaturebackend.support.exception.ErrorCode
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class FindNovelStoryArcsUseCase(
    private val novelService: NovelService,
    private val contributorService: ContributorService,
) : UseCase<FindNovelStoryArcsUseCase.Request, List<FindNovelStoryArcsUseCase.Response>> {

    @Transactional(readOnly = true)
    override fun execute(request: Request, executedAt: LocalDateTime): List<Response> {
        val contributorGroup = findContributorGroup(request.novelRoomId)
        validateParticipantAccess(contributorGroup, request.accountId)
        val novel = findNovel(contributorGroup.novelId)

        return novel.storyArcs.map { storyArc ->
            Response(
                id = storyArc.id.toString(),
                description = storyArc.description,
                phase = storyArc.phase,
                phaseAlias = storyArc.phase.koreanName,
                firstChapterNumber = storyArc.firstChapterNumber,
                lastChapterNumber = storyArc.lastChapterNumber,
            )
        }
    }

    private fun findContributorGroup(novelRoomId: String): ContributorGroup =
        contributorService.findGroupById(ContributorGroupId.from(novelRoomId))
            ?: throw BusinessException(ErrorCode.CONTRIBUTOR_GROUP_NOT_FOUND)

    private fun validateParticipantAccess(contributorGroup: ContributorGroup, accountId: String) {
        if (!contributorGroup.isParticipating(AccountId.from(accountId))) {
            throw BusinessException(ErrorCode.NO_PERMISSION_TO_VIEW)
        }
    }

    private fun findNovel(novelId: NovelId): Novel =
        novelService.findNovel(novelId)
            ?: throw BusinessException(ErrorCode.NOVEL_NOT_FOUND)

    data class Response(
        val id: String,
        val description: String?,
        val phase: StoryPhase,
        val phaseAlias: String,
        val firstChapterNumber: Int?,
        val lastChapterNumber: Int?,
    )

    data class Request(
        val accountId: String,
        val novelRoomId: String
    )
}
