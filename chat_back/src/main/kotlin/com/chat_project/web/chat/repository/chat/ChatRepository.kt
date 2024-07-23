package com.chat_project.web.chat.repository.chat

import com.chat_project.web.chat.entity.Chat
import com.chat_project.web.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository: JpaRepository<Chat, Long> {
    fun findByMemberEmail(memberEmail: String): List<Chat>
    @Query("SELECT" +
            "new com.chat_project.web.chat.dto.ChatDTO(" +
            ") " +
            "FROM Chat c WHERE c.chatRoom.id = :chatRoomId")
    fun findByChatRoomId(chatRoomId: Long): MutableList<Chat>
}