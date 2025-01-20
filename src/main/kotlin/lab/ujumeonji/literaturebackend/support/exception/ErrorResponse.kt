package lab.ujumeonji.literaturebackend.support.exception

data class ErrorResponse(
    val status: Int,
    val code: String,
    val message: String
)
