package lab.ujumeonji.literaturebackend.usecase.novel

import lab.ujumeonji.literaturebackend.domain.contributor.ContributorGroupStatus
import lab.ujumeonji.literaturebackend.domain.contributor.ContributorService
import lab.ujumeonji.literaturebackend.domain.contributor.command.CreateContributorGroupCommand
import lab.ujumeonji.literaturebackend.domain.novel.NovelService
import lab.ujumeonji.literaturebackend.domain.novel.command.CreateNovelCommand
import lab.ujumeonji.literaturebackend.usecase.UseCase
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
@Transactional
class CreateNovelRoomsUseCase(
    private val contributorService: ContributorService,
    private val novelService: NovelService,
) : UseCase<CreateNovelRoomsUseCase.Request, CreateNovelRoomsUseCase.Response> {

    override fun execute(request: Request, executedAt: LocalDateTime): Response {
        return Response(emptyList())
    }

    data class Request(
        val accountId: Long,
    )

    /*
        {
            "id": 4,
            "category": {
                "id": 9,
                "name": "공포/추리"
            },
            "title": "테스트 소설공방",
            "createdAt": "2025-01-11 15:23",
            "writerStatus": "attending",
            "writerCategory": "host",
            "type": 5,
            "currentAttendCnt": 1,
            "currentWriter": "테스트용진",
            "status": "prepare",
            "notifiedAt": null,
            "exitedAt": null,
            "completedAt": null
        },
     */
    data class Response(
        val result: List<ResponseItem>
    ) {
        data class ResponseItem(
            val id: Long,
            val category: Category,
            val title: String,
            val createdAt: LocalDateTime,
            val status: ContributorGroupStatus,
            val currentContributorCount: Int,
        ) {
            data class Category(
                val name: String,
                val alias: String,
            )
        }
    }
}
