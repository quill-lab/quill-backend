package lab.ujumeonji.literaturebackend.support.http

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T,
)
