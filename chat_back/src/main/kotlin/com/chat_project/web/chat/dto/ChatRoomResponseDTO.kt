package com.chat_project.web.chat.dto

import java.time.LocalDateTime

data class ChatRoomResponseDTO(
    var id: Long? = 0,
    var roomName: String? = "",
    var numberPeople: Int? = 0,
    var registerDate: LocalDateTime? = LocalDateTime.now(),
    var updateDate: LocalDateTime? = LocalDateTime.now(),
    var lastMessage: String? = "",
    var lastSendDate: LocalDateTime? = LocalDateTime.now(),
    val memberCount: Long? = 0
)
