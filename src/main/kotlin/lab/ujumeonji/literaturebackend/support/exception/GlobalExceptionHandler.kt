package lab.ujumeonji.literaturebackend.support.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        val errorCode = e.errorCode
        val response = ErrorResponse(
            status = errorCode.status,
            code = errorCode.code,
            message = errorCode.message
        )

        return ResponseEntity(response, HttpStatus.valueOf(errorCode.status))
    }
}
