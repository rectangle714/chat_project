package com.chat_project.web.chat.service

import com.chat_project.web.chat.dto.ChatRoomResponseDTO
import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.chat.repository.chatRoom.ChatRoomRepository
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException
import java.util.Optional

@Service
@Transactional(rollbackFor = [ Exception::class ])
class ChatRoomService(
    private val chatRoomRepository: ChatRoomRepository,
    private val mapper: ModelMapper
) {
    @Transactional(readOnly = true)
    fun getChatRoomListPaging(pageable: Pageable): Page<ChatRoomResponseDTO> {
        return chatRoomRepository.getChatRoomList(pageable)
    }

    @Transactional(readOnly = true)
    fun getChatRoomInfo(roomId: String): Optional<ChatRoom> {
        return chatRoomRepository.findById(roomId.toLong());
    }

    fun addChatRoom(chatRoomDTO: ChatRoomResponseDTO): String {
        chatRoomRepository.save(mapper.map(chatRoomDTO, ChatRoom::class.java))
        return "success"
    }

    fun updateChatRoom(chatRoomDTO: ChatRoomResponseDTO): String {
        val chatRoom: ChatRoom = chatRoomRepository.findById(chatRoomDTO.id)
            .orElseThrow { IllegalArgumentException("채팅방을 찾을 수 없습니다. ID: ${chatRoomDTO.id}") }

        chatRoom.update(chatRoomDTO.roomName!!, chatRoomDTO.numberPeople)
        return "success"
    }
}