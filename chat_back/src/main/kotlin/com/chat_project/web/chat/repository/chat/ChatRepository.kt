package com.chat_project.web.chat.repository.chat

import com.chat_project.web.chat.entity.Chat
import com.chat_project.web.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository: JpaRepository<Chat, Long> {
    fun findByMemberEmail(memberEmail: String): List<Chat>
    fun findByChatRoomId(chatRoomId: Long): MutableList<Chat>
}