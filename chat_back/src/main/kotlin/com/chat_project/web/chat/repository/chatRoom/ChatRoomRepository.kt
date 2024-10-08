package com.chat_project.web.chat.repository.chatRoom

import com.chat_project.web.chat.entity.ChatRoom
import org.springframework.data.jpa.repository.JpaRepository

interface ChatRoomRepository: JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    /*@Query("SELECT " +
                "new com.chat_project.web.chat.dto.ChatRoomDTO(" +
                "cr.id, cr.roomName, cr.numberPeople, cr.registerDate, cr.updateDate) " +
            "FROM ChatRoom cr")
    fun getChatroomList(): List<ChatRoomResponseDTO>*/
}