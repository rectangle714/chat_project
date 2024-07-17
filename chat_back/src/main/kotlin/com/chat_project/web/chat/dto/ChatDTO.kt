package com.chat_project.web.chat.dto

data class ChatDTO(
    val chatId: Long? = 0,
    val chatRoomId: Long? = 0,
    val memberId: Long? = 0,
    var message: String = "",
    val sender: String = "",
    val chatType: String = ""
)