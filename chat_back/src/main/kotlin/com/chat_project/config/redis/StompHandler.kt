package com.chat_project.config.redis

import com.chat_project.common.util.logger
import com.chat_project.common.util.RedisUtil
import com.chat_project.exception.CustomException
import com.chat_project.exception.CustomExceptionCode
import com.chat_project.security.TokenProvider
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
    private val redisUtil: RedisUtil
): ChannelInterceptor {
    val logger = logger()

    /* websocket 요청 전 처리 */
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor: StompHeaderAccessor = StompHeaderAccessor.wrap(message)
        val payload = message.payload

        if(StompCommand.CONNECT == accessor.command) {
            logger.info("Socket Connect")
            val token: String? = accessor.getFirstNativeHeader("Authorization")
            if(token.isNullOrEmpty()) {
                throw CustomException(CustomExceptionCode.BAD_TOKEN_INFO)
            } else {
                val user = tokenProvider.parseTokenInfo(token)
                if(Objects.nonNull(redisUtil.getData(user.username))) {
                    UsernamePasswordAuthenticationToken.authenticated(user, token, user.authorities)
                        .also { SecurityContextHolder.getContext().authentication = it }
                } else {
                    throw CustomException(CustomExceptionCode.BAD_TOKEN_INFO)
                }
            }

            val roomId: String? = accessor.getFirstNativeHeader("room_id")
            val email = tokenProvider.parseTokenInfo(token).username

            val memberDTO: MemberDTO = memberService.getMemberInfo(email)
            var chatRoomDTO: ChatRoomResponseDTO
            var chatRoom: ChatRoom? = null

            roomId
                ?. let{
                    chatRoom = chatRoomService.getChatRoomInfo(roomId)
                        .orElseThrow { IllegalArgumentException("채팅방 ID 값 NULL") }
                } .run {
                    chatRoomDTO = modelMapper.map(chatRoom, ChatRoomResponseDTO::class.java)
                    chatRoomDTO
                }

            chatRoomMemberRepository.findByMemberIdAndChatRoomId(memberDTO.memberId, chatRoomDTO.id!!)
                ?: run {
                    val member: Member = modelMapper.map(memberDTO, Member::class.java)
                    chatRoomMemberRepository.save(ChatRoomMember(member, chatRoom!!))
                    chatRepository.save(Chat("'${member.nickname}'님이 입장 했습니다.", member, chatRoom!!, "Y"))
                }

        } else if(StompCommand.DISCONNECT == accessor.command) {
            logger.info("Socket Disconnect")
        }

        return message
    }
}