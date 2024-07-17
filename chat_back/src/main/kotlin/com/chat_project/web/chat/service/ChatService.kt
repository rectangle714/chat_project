package com.chat_project.web.chat.service

import com.chat_project.web.chat.dto.ChatDTO
import com.chat_project.web.chat.entity.Chat
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.chat.repository.chatRoom.ChatRoomRepository
import com.chat_project.web.member.repository.MemberRepository
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
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
    fun getChatList(chatRoomId: Long): MutableList<Chat> {
        return chatRepository.findByChatRoomId(chatRoomId)
    }

    fun addChat(user: User, chatDTO: ChatDTO) {
        val member = memberRepository.findByEmail(user.username)
    }
}