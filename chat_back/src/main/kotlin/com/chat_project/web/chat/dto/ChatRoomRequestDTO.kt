package com.chat_project.web.chat.dto

import com.chat_project.web.chat.enums.RoomType

data class ChatRoomRequestDTO(
    var id: Long = 0,
    var roomName: String = "",
    var numberPeople: Int = 0,
    var email: String = "",
    var roomType: String = ""
)