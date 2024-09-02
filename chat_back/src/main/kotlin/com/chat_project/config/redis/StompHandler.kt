package com.chat_project.config.redis

import com.chat_project.common.util.logger
import com.chat_project.common.util.RedisUtil
import com.chat_project.exception.CustomException
import com.chat_project.exception.CustomExceptionCode
import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.dto.ChatRequestDTO
import com.chat_project.web.chat.dto.ChatResponseDTO
import com.chat_project.web.chat.dto.ChatRoomResponseDTO
import com.chat_project.web.chat.entity.Chat
import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.chat.entity.ChatRoomMember
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.chat.repository.chatRoomMate.ChatRoomMemberRepository
import com.chat_project.web.chat.service.ChatRoomService
import com.chat_project.web.member.dto.MemberDTO
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import com.chat_project.web.member.service.MemberService
import io.jsonwebtoken.ExpiredJwtException
import org.modelmapper.ModelMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.*
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.util.*

@Component
class StompHandler(
    private val tokenProvider: TokenProvider,
    private val memberService: MemberService,
    private val chatRoomService: ChatRoomService,
    private val modelMapper: ModelMapper,
    private val chatRoomMemberRepository: ChatRoomMemberRepository,
    private val chatRepository: ChatRepository,
    private val redisUtil: RedisUtil,
    private val redisTemplate: StringRedisTemplate,
    private val channelTopic: ChannelTopic,
): ChannelInterceptor {
    val logger = logger()

    /* websocket 요청 전 처리 */
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>?
        = message
}