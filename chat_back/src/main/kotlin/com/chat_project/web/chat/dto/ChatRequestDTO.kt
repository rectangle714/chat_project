package com.chat_project.web.chat.dto

data class ChatRequestDTO(
    val chatRoomId: Long? = 0,
    val memberId: Long? = 0,
    var message: String = "",
    var sender: String = "",
    val accessToken: String = "",
    var alert: String= ""
)
