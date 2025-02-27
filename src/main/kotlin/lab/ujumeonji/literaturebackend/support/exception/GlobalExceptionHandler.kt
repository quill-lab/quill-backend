package lab.ujumeonji.literaturebackend.support.exception

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(e: BusinessException): ResponseEntity<ErrorResponse> {
        val errorCode = e.errorCode
        val errorResponse = ErrorResponse(
            status = errorCode.status,
            code = errorCode.code,
            message = errorCode.message
        )

        return ResponseEntity(errorResponse, HttpStatus.valueOf(errorCode.status))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            code = ErrorCode.BAD_REQUEST.code,
            message = "Validation error: ${ex.message}"
        )

        return ResponseEntity(errorResponse, HttpStatus.BAD_REQUEST)
    }
}
