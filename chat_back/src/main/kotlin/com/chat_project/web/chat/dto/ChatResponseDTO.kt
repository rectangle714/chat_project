package com.chat_project.web.chat.dto

import java.time.LocalDateTime

data class ChatResponseDTO(
    val chatId: Long? = 0,
    var message: String = "",
    var sender: String = "",
    var registerDate: String = "",
    var isAlert: String = "",
    var isFile: String = "",
    var fileId: Long = 0
)