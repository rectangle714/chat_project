package com.chat_project.handler

import com.chat_project.common.dto.ApiResponse
import com.chat_project.common.util.logger
import com.chat_project.exception.CustomException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    val logger = logger()

    @ExceptionHandler(IllegalArgumentException::class, NoSuchElementException::class, CustomException::class)
    fun commonException(e: Exception): ResponseEntity<ApiResponse> {
        logger.error("commonException 발생: {}", e.message)
        return ResponseEntity.badRequest().body(ApiResponse.error(e.message))
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun accessDeniedException(e: AccessDeniedException): ResponseEntity<ApiResponse> {
        logger.error("AccessDeniedException 발생: {}", e.message)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.error("접근이 거부되었습니다."))
    }

    @ExceptionHandler(Exception::class)
    fun unexpectedException(e: Exception): ResponseEntity<ApiResponse> {
        logger.error("Exception 발생: {}", e.message)
        return ResponseEntity.internalServerError().body(ApiResponse.error("서버에 문제가 발생했습니다."))
    }

    @ExceptionHandler(SignatureException::class)
    fun signatureException(e: SignatureException): ResponseEntity<ApiResponse> {
        logger.error("SignatureException 발생: {}", e.message)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 유효하지 않습니다."))
    }

    @ExceptionHandler(MalformedJwtException::class)
    fun malformedJwtException(e: MalformedJwtException): ResponseEntity<ApiResponse> {
        logger.error("MalformedJwtException 발생: {}", e.message)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("올바르지 않은 토큰입니다."))
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun expiredJwtException(e: ExpiredJwtException): ResponseEntity<ApiResponse> {
        logger.error("expiredJwtException 발생: {}", e.message)
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.error("토큰이 만료되었습니다. 다시 로그인해주세요."))
    }
}