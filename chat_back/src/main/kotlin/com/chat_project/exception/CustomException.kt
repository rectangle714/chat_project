package com.chat_project.exception

data class CustomException(val exceptionCode: CustomExceptionCode): Exception() {
    override val message: String
        get() = exceptionCode.message
}