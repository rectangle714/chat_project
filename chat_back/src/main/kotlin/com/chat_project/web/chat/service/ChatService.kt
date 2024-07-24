package com.chat_project.web.chat.service

import com.chat_project.web.chat.dto.ChatResponseDTO
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.member.repository.MemberRepository
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [ Exception::class ])
class ChatService(
    private val memberRepository: MemberRepository,
    private val chatRepository: ChatRepository
) {
    @Transactional(readOnly = true)
    fun getChatList(chatRoomId: Long): MutableList<ChatResponseDTO> {
        return chatRepository.findChattingList(chatRoomId)
    }
}