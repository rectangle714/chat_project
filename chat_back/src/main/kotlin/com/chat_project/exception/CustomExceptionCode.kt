package com.chat_project.exception

import org.springframework.http.HttpStatus

enum class CustomExceptionCode(
    val status: Int,
    val message: String,
) {
    /* 사용자 관련 Exception */
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST.value(),"사용자를 찾을 수 없습니다."),
    BAD_TOKEN_INFO(HttpStatus.UNAUTHORIZED.value(),"유효하지 않은 토큰입니다."),
    BAD_REFRESH_TOKEN_INFO(HttpStatus.UNAUTHORIZED.value(), "잘못된 refresh 토큰 정보 입니다."),

    /* 채팅방 관련 Exception */
    CHAT_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "해당 채팅방을 찾을 수 없습니다."),
}