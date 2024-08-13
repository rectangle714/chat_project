package com.chat_project.web.chat.repository.chatRoom

import com.chat_project.web.chat.dto.ChatRoomResponseDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ChatRoomRepositoryCustom {
    fun getChatRoomList(pageable: Pageable): Page<ChatRoomResponseDTO>;
}