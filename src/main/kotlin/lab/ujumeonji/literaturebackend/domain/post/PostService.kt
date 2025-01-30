package lab.ujumeonji.literaturebackend.domain.post

import lab.ujumeonji.literaturebackend.domain.post.command.CreateNovelRoomRecruitmentPostCommand
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostService @Autowired constructor(
    private val contributorGroupRecruitmentRepository: ContributorGroupRecruitmentRepository,
) : NovelRoomRecruitmentService {

    override fun createNovelRecruitmentPost(
        command: CreateNovelRoomRecruitmentPostCommand,
        now: LocalDateTime
    ): ContributorGroupRecruitment =
        contributorGroupRecruitmentRepository.save(
            ContributorGroupRecruitment.create(
                command.contributorGroupId,
                command.authorId,
                command.title,
                command.content,
                command.link,
                now,
            )
        )
}
