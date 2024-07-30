package com.chat_project.web.chat.repository.chatRoom

import com.chat_project.web.chat.dto.ChatRoomResponseDTO

interface ChatRoomRepositoryCustom {
    fun getChatRoomList(): MutableList<ChatRoomResponseDTO>;
}