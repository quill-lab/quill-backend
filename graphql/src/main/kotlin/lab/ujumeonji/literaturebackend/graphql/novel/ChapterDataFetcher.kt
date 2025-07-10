package lab.ujumeonji.literaturebackend.graphql.novel

import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument
import lab.ujumeonji.literaturebackend.domain.novel.command.ChapterStatusEnum
import lab.ujumeonji.literaturebackend.graphql.auth.RequiredGraphQLAuth
import lab.ujumeonji.literaturebackend.graphql.generated.types.*
import lab.ujumeonji.literaturebackend.usecase.novel.FindChapterUseCase
import lab.ujumeonji.literaturebackend.usecase.novel.FindChaptersUseCase
import lab.ujumeonji.literaturebackend.usecase.novel.FindNovelEpisodesUseCase
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@DgsComponent
class ChapterDataFetcher(
    private val findChaptersUseCase: FindChaptersUseCase,
    private val findChapterUseCase: FindChapterUseCase,
    private val findNovelEpisodesUseCase: FindNovelEpisodesUseCase,
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
                    editedAt = chapterItem.editedAt,
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
                    approvedAt = chapterItem.approvedAt,
                    currentAuthor =
                        chapterItem.currentAuthor?.let {
                            Author(it.id, it.name, it.accountId)
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

    @DgsQuery
    fun chapter(
        @RequiredGraphQLAuth accountId: String,
        @InputArgument id: String,
    ): Chapter? {
        try {
            val result =
                findChapterUseCase.execute(
                    request =
                        FindChapterUseCase.Request(
                            chapterId = id,
                            accountId = accountId,
                        ),
                    executedAt = LocalDateTime.now(),
                )

            return Chapter(
                id = result.id,
                title = result.title,
                editedAt = result.editedAt.atOffset(ZoneOffset.UTC),
                episode = result.episode,
                status =
                    when (result.status) {
                        ChapterStatusEnum.DRAFT -> ChapterStatus.DRAFT
                        ChapterStatusEnum.IN_PROGRESS -> ChapterStatus.IN_PROGRESS
                        ChapterStatusEnum.REQUESTED -> ChapterStatus.REQUESTED
                        ChapterStatusEnum.APPROVED -> ChapterStatus.APPROVED
                        ChapterStatusEnum.CANCELLED -> ChapterStatus.CANCELLED
                        ChapterStatusEnum.REJECTED -> ChapterStatus.REJECTED
                    },
                approvedAt = result.approvedAt?.atOffset(ZoneOffset.UTC),
                currentAuthor =
                    result.currentAuthor?.let {
                        Author(it.id, it.name, it.accountId)
                    },
                metadata =
                    ChapterMetadata(
                        viewCount = result.metadata.viewCount,
                        commentCount = result.metadata.commentCount,
                        likeCount = result.metadata.likeCount,
                    ),
            )
        } catch (e: Exception) {
            return null
        }
    }

    @DgsQuery
    fun novelEpisodes(
        @InputArgument novelId: String,
        @InputArgument offset: Int = 0,
        @InputArgument limit: Int = 20,
    ): EpisodeConnection {
        val result =
            findNovelEpisodesUseCase.execute(
                FindNovelEpisodesUseCase.Request(
                    novelId = novelId,
                    offset = offset,
                    limit = limit,
                ),
                executedAt = LocalDateTime.now(),
            )

        val nodes =
            result.chapters.map { chapterItem ->
                Episode(
                    id = chapterItem.id,
                    episode = chapterItem.episode,
                    title = chapterItem.title,
                    metadata =
                        ChapterMetadata(
                            viewCount = chapterItem.metadata.viewCount,
                            commentCount = chapterItem.metadata.commentCount,
                            likeCount = chapterItem.metadata.likeCount,
                        ),
                )
            }

        val edges =
            nodes.map { node ->
                EpisodeEdge(
                    node = node,
                    cursor = Base64.getEncoder().encodeToString(node.id.toByteArray()),
                )
            }

        return EpisodeConnection(
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
