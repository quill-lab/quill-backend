package lab.ujumeonji.literaturebackend.support.exception

open class BusinessException(
    val errorCode: ErrorCode,
) : RuntimeException(errorCode.message)
