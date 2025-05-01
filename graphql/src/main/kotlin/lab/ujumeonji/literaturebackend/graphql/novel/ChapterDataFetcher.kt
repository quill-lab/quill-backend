package lab.ujumeonji.literaturebackend.graphql.novel

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import lab.ujumeonji.literaturebackend.domain.novel.command.ChapterStatusEnum
import lab.ujumeonji.literaturebackend.graphql.auth.RequiredGraphQLAuth
import lab.ujumeonji.literaturebackend.graphql.generated.types.*
import lab.ujumeonji.literaturebackend.usecase.novel.FindChaptersUseCase
import java.time.LocalDateTime
import java.util.*
import java.time.ZoneOffset

@DgsComponent
class ChapterDataFetcher(
    private val findChaptersUseCase: FindChaptersUseCase,
) {
    @DgsQuery
    fun chaptersConnection(
        @RequiredGraphQLAuth accountId: String,
        @InputArgument contributorGroupId: String,
        @InputArgument offset: Int = 0,
        @InputArgument limit: Int = 20,
    ): ChapterConnection {
        val result =
            findChaptersUseCase.execute(
                request =
                    FindChaptersUseCase.Request(
                        accountId = accountId,
                        contributorGroupId = contributorGroupId,
                        offset = offset,
                        limit = limit,
                    ),
                executedAt = LocalDateTime.now(),
            )

        val nodes =
            result.chapters.map { chapterItem ->
                Chapter(
                    id = chapterItem.id,
                    title = chapterItem.title,
                    editedAt = chapterItem.editedAt.atOffset(ZoneOffset.UTC),
                    episode = chapterItem.episode,
                    status =
                        when (chapterItem.status) {
                            ChapterStatusEnum.DRAFT -> ChapterStatus.DRAFT
                            ChapterStatusEnum.IN_PROGRESS -> ChapterStatus.IN_PROGRESS
                            ChapterStatusEnum.REQUESTED -> ChapterStatus.REQUESTED
                            ChapterStatusEnum.APPROVED -> ChapterStatus.APPROVED
                            ChapterStatusEnum.CANCELLED -> ChapterStatus.CANCELLED
                            ChapterStatusEnum.REJECTED -> ChapterStatus.REJECTED
                        },
                    approvedAt = chapterItem.approvedAt?.atOffset(ZoneOffset.UTC),
                    currentAuthor =
                        chapterItem.currentAuthor?.let {
                            Author(it.id, it.name)
                        },
                    metadata =
                        ChapterMetadata(
                            viewCount = 0,
                            commentCount = 0,
                            likeCount = 0,
                        ),
                )
            }

        val edges =
            nodes.map { node ->
                ChapterEdge(
                    node = node,
                    cursor = Base64.getEncoder().encodeToString(node.id.toByteArray()),
                )
            }

        return ChapterConnection(
            edges = edges,
            pageInfo =
                PageInfo(
                    offset = offset,
                    limit = limit,
                ),
            nodes = nodes,
            totalCount = result.totalCount,
        )
    }
}
