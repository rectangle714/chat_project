package com.chat_project.web.chat.dto

import org.springframework.web.multipart.MultipartFile

data class ChatRequestDTO(
    val chatRoomId: Long? = 0,
    val memberId: Long? = 0,
    var message: String = "",
    var sender: String = "",
    val accessToken: String = "",
    var alert: String= "",
    val file: List<MultipartFile>? = null
)
