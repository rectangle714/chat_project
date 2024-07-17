package com.chat_project.web.chat.dto

import java.time.LocalDateTime

data class ChatRoomDTO(
    var id: Long?,
    var roomName: String?,
    var numberPeople: Int,
    var registerDate: LocalDateTime?,
    var updateDate: LocalDateTime?,
)
