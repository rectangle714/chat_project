package com.chat_project.web.chat.repository.chat

import com.chat_project.web.chat.dto.ChatResponseDTO

interface ChatRepositoryCustom {
    fun findChattingList(chatRoomId: Long): MutableList<ChatResponseDTO>
}