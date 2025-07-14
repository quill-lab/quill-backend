package lab.ujumeonji.literaturebackend.domain.post

import lab.ujumeonji.literaturebackend.domain.post.command.CreateNovelRoomRecruitmentPostCommand
import java.time.LocalDateTime

interface ContributorGroupRecruitmentService {
    fun createNovelRecruitmentPost(
        command: CreateNovelRoomRecruitmentPostCommand,
        now: LocalDateTime,
    ): ContributorGroupRecruitment

    fun findRecruitmentById(id: ContributorGroupRecruitmentId): ContributorGroupRecruitment?
}
