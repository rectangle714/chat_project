package com.chat_project.web.message

import com.chat_project.common.constant.ChatType
import com.chat_project.common.util.logger
import com.chat_project.exception.CustomException
import com.chat_project.exception.CustomExceptionCode
import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.dto.ChatRequestDTO
import com.chat_project.web.chat.entity.Chat
import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.chat.entity.ChatRoomMember
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.chat.repository.chatRoom.ChatRoomRepository
import com.chat_project.web.chat.repository.chatRoomMate.ChatRoomMemberRepository
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import com.chat_project.web.member.service.MemberService
import org.modelmapper.ModelMapper
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [ Exception::class ])
class MessageService(
    private val chatRepository: ChatRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomMemberRepository: ChatRoomMemberRepository,
    private val memberRepository: MemberRepository,
    private val memberService: MemberService,
    private val channelTopic: ChannelTopic,
    private val redisTemplate: StringRedisTemplate,
    private val modelMapper: ModelMapper,
    private val tokenProvider: TokenProvider
) {
    fun sendMessage(ChatRequestDTO: ChatRequestDTO) {
        val user = tokenProvider.parseTokenInfo(ChatRequestDTO.accessToken)
        val member: Member =  user.username
                                .let { memberRepository.findByEmail(it) }
                                ?: throw CustomException(CustomExceptionCode.NOT_FOUND_MEMBER)
        val chatRoom: ChatRoom = ChatRequestDTO.chatRoomId
            ?.let { chatRoomRepository.findById(it).get() }
            ?: throw CustomException(CustomExceptionCode.CHAT_ROOM_NOT_FOUND)

        ChatRequestDTO.sender = member.nickname
        val chat: Chat = Chat(ChatRequestDTO.message, member, chatRoom);
        chatRepository.save(chat)

        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(String::class.java)
        redisTemplate.convertAndSend(channelTopic.topic, ChatRequestDTO)
    }

    fun exitChatRoom(accessToken: String, roomId: Long) {
        val user = tokenProvider.parseTokenInfo(accessToken)
        val member: Member = user.username
            .let { memberRepository.findByEmail(it) }
            ?: run { throw CustomException(CustomExceptionCode.NOT_FOUND_MEMBER) }
        val chatRoom: ChatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow { CustomException(CustomExceptionCode.CHAT_ROOM_NOT_FOUND) }

        chatRoomMemberRepository.findByMemberIdAndChatRoomId(member.id, chatRoom.id!!)
            ?. let {
                chatRoomMemberRepository.delete(it)
            }
    }
}