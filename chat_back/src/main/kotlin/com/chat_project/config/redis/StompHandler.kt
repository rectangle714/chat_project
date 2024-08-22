package com.chat_project.config.redis

import com.chat_project.common.util.logger
import com.chat_project.common.util.RedisUtil
import com.chat_project.exception.CustomException
import com.chat_project.exception.CustomExceptionCode
import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.dto.ChatResponseDTO
import com.chat_project.web.member.repository.MemberRepository
import io.jsonwebtoken.ExpiredJwtException
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
import java.util.*

@Component
class StompHandler(
    private val tokenProvider: TokenProvider,
    private val memberRepository: MemberRepository,
    private val redisUtil: RedisUtil
): ChannelInterceptor {
    val logger = logger()

    /* websocket 요청 전 처리 */
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor: StompHeaderAccessor = StompHeaderAccessor.wrap(message)
        val payload: Any = message.payload

        if(StompCommand.CONNECT == accessor.command) {
            logger.info("Socket Connect")

            val token: String? = accessor.getFirstNativeHeader("Authorization")
            val user = tokenProvider.parseTokenInfo(token)

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
        } else if(StompCommand.DISCONNECT == accessor.command) {
            logger.info("Socket Disconnect")
        }

        return message
    }
}