package com.chat_project.web.chat.repository.chat

import com.chat_project.web.chat.dto.ChatResponseDTO
import com.chat_project.web.chat.entity.Chat
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository: JpaRepository<Chat, Long>, ChatRepositoryCustom {
    fun findByMemberEmail(memberEmail: String): List<Chat>
    override fun findChattingList(chatRoomId: Long): MutableList<ChatResponseDTO>
}