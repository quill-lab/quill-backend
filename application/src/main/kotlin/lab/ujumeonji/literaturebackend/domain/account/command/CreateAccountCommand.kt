package lab.ujumeonji.literaturebackend.domain.account.command

data class CreateAccountCommand(
    val email: String,
    val password: String,
    val nickname: String
)
