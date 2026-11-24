package com.molt.exception

import org.springframework.http.HttpStatus

class ApiException(
    val status: HttpStatus,
    val code: String,
    message: String,
) : RuntimeException(message) {

    companion object {
        fun notFound(message: String): ApiException = ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", message)
    }
}
