package lab.ujumeonji.literaturebackend.support.exception

enum class ErrorCode(
    val status: Int,
    val code: String,
    val message: String
) {
    // Contributor 관련 에러
    DUPLICATE_CONTRIBUTOR_GROUP(400, "CONTRIBUTOR-001", "이미 기여자 그룹을 가지고 있는 사용자입니다."),
    CONTRIBUTOR_GROUP_NOT_FOUND(404, "CONTRIBUTOR-002", "기여자 그룹을 찾을 수 없습니다."),
    NO_PERMISSION_TO_UPDATE(403, "CONTRIBUTOR-003", "소설을 수정할 권한이 없습니다."),

    // Account 관련 에러
    ACCOUNT_NOT_FOUND(404, "ACCOUNT-001", "계정을 찾을 수 없습니다."),

    // Novel 관련 에러
    NOVEL_NOT_FOUND(404, "NOVEL-001", "소설을 찾을 수 없습니다."),

    // Auth 관련 에러
    INVALID_CREDENTIALS(401, "AUTH-001", "이메일 또는 비밀번호가 올바르지 않습니다."),
    DUPLICATE_EMAIL(409, "AUTH-002", "이미 사용중인 이메일입니다."),
}
