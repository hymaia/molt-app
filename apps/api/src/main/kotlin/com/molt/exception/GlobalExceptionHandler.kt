package com.molt.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.resource.NoResourceFoundException

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ApiException::class)
    fun handleApi(e: ApiException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(e.status).body(ErrorResponse(e.code, e.message))
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleStatus(e: ResponseStatusException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.valueOf(e.statusCode.value())
        val code = if (status == HttpStatus.BAD_REQUEST) "VALIDATION_ERROR" else status.name
        return ResponseEntity.status(status).body(ErrorResponse(code, e.reason))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val msg = e.bindingResult.fieldErrors.joinToString(", ") { fe -> fe.field + " " + fe.defaultMessage }
        return ResponseEntity.badRequest().body(ErrorResponse("VALIDATION_ERROR", msg))
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.badRequest()
            .body(ErrorResponse("VALIDATION_ERROR", "Invalid value for parameter " + e.name))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleUnreadable(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.badRequest().body(ErrorResponse("VALIDATION_ERROR", "Malformed request body"))
    }

    @ExceptionHandler(NoResourceFoundException::class)
    fun handleNoResource(e: NoResourceFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse("NOT_FOUND", "Resource not found"))
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethod(e: HttpRequestMethodNotSupportedException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(ErrorResponse("METHOD_NOT_ALLOWED", e.message))
    }

    @ExceptionHandler(Exception::class)
    fun handleOther(e: Exception): ResponseEntity<ErrorResponse> {
        log.error("Unexpected error", e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse("INTERNAL_ERROR", "Something went wrong"))
    }

    companion object {
        private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)
    }
}
