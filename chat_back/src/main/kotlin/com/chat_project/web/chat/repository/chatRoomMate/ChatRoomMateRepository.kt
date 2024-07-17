package com.chat_project.web.chat.repository.chatRoomMate

import com.chat_project.web.chat.entity.ChatRoomMember
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomMateRepository: JpaRepository<ChatRoomMember, Long> {

}