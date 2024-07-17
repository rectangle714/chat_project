package com.chat_project.web.message

import com.chat_project.common.constant.ChatType
import com.chat_project.common.util.logger
import com.chat_project.exception.CustomException
import com.chat_project.exception.CustomExceptionCode
import com.chat_project.web.chat.dto.ChatDTO
import com.chat_project.web.chat.entity.Chat
import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.chat.entity.ChatRoomMember
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.chat.repository.chatRoom.ChatRoomRepository
import com.chat_project.web.chat.repository.chatRoomMate.ChatRoomMateRepository
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
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
    private val chatRoomMateRepository: ChatRoomMateRepository,
    private val memberRepository: MemberRepository,
    private val channelTopic: ChannelTopic,
    private val redisTemplate: StringRedisTemplate,
    private val modelMapper: ModelMapper
) {
    fun sendMessage(chatDTO: ChatDTO) {
        val chatRoom: ChatRoom = chatDTO.chatRoomId
                                    ?.let { chatRoomRepository.findById(it).get() }
                                    ?: throw CustomException(CustomExceptionCode.CHAT_ROOM_NOT_FOUND)
        val member: Member = chatDTO.sender
                                .let { memberRepository.findByEmail(it) }
                                ?: throw CustomException(CustomExceptionCode.NOT_FOUND_MEMBER)

        if(chatDTO.chatType == ChatType.ENTER.name) {
            logger().info("채팅방 입장")
            chatRoomMateRepository.save(ChatRoomMember(member, chatRoom))
            chatDTO.message = chatDTO.sender + "님이 입장했습니다."
        } else if(chatDTO.chatType == ChatType.SEND.name) {
            logger().info("채팅 발송")
        }

        val chat: Chat = Chat(chatDTO.message, member, chatRoom);
        chatRepository.save(chat)

        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(String::class.java)
        redisTemplate.convertAndSend(channelTopic.topic, modelMapper.map(chat, ChatDTO::class.java))
    }
}