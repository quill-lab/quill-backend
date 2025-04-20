package lab.ujumeonji.literaturebackend.support.exception

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String,
) {
    // Contributor 관련 에러
    DUPLICATE_CONTRIBUTOR_GROUP(400, "CONTRIBUTOR-001", "이미 기여자 그룹을 가지고 있는 사용자입니다."),
    CONTRIBUTOR_GROUP_NOT_FOUND(404, "CONTRIBUTOR-002", "기여자 그룹을 찾을 수 없습니다."),
    NO_PERMISSION_TO_UPDATE(403, "CONTRIBUTOR-003", "수정할 권한이 없습니다."),
    NO_PERMISSION_TO_VIEW(403, "CONTRIBUTOR-004", "조회 권한이 없습니다."),
    CONTRIBUTOR_NOT_FOUND(404, "CONTRIBUTOR-005", "기여자를 찾을 수 없습니다."),
    NOT_YOUR_TURN_TO_WRITE(403, "CONTRIBUTOR-006", "현재 글 작성 순서가 아닙니다."),
    CONTRIBUTOR_GROUP_EMPTY(400, "CONTRIBUTOR-007", "기여자 그룹에 참여자가 없습니다."),

    // Account 관련 에러
    ACCOUNT_NOT_FOUND(404, "ACCOUNT-001", "계정을 찾을 수 없습니다."),
    EMAIL_NOT_FOUND(404, "ACCOUNT-002", "존재하지 않는 이메일입니다."),

    // Novel 관련 에러
    NOVEL_NOT_FOUND(404, "NOVEL-001", "소설을 찾을 수 없습니다."),
    CURRENT_CHAPTER_NOT_EDITABLE(400, "NOVEL-002", "현재 회차는 현재 수정할 수 없는 상태입니다."),
    CHAPTER_ALREADY_REQUESTED(400, "NOVEL-003", "이미 연재 신청 중인 챕터가 존재합니다."),
    CHAPTER_NOT_FOUND(404, "NOVEL-004", "챕터를 찾을 수 없습니다."),

    // Auth 관련 에러
    UNAUTHORIZED(401, "AUTH-000", "로그인 후 서비스 이용이 가능합니다."),
    INVALID_CREDENTIALS(401, "AUTH-001", "이메일 또는 비밀번호가 올바르지 않습니다."),
    DUPLICATE_EMAIL(409, "AUTH-002", "이미 사용중인 이메일입니다."),

    // Common
    BAD_REQUEST(400, "REQ-001", "잘못된 요청입니다."),
}
