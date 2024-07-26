package com.chat_project.config.redis

import com.chat_project.common.util.logger
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.messaging.Message
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler
import java.nio.charset.StandardCharsets

@Component
class StompErrorHandler(): StompSubProtocolErrorHandler() {
    val logger = logger()

    override fun handleClientMessageProcessingError(
        clientMessage: Message<ByteArray>?,
        ex: Throwable
    ): Message<ByteArray>? {
        logger.error("메세지 에러 발생: {}",ex.cause?.message)
        when(ex) {
            is ExpiredJwtException -> {
                logger.error("Jwt 토큰 만료 : {}",ex.cause?.message)

            }
            else -> {
                logger.error("메세지 에러 발생: {}",ex.cause?.message)
                return prepareErrorMessage(ex.cause?.message)
            }

        }
        return prepareErrorMessage(ex.cause?.message)
    }

    private fun prepareErrorMessage (error: String?): Message<ByteArray> {
        val accessor: StompHeaderAccessor = StompHeaderAccessor.create(StompCommand.ERROR)
        accessor.message = error
        accessor.setLeaveMutable(true)

        return MessageBuilder.createMessage(error!!.toByteArray(StandardCharsets.UTF_8), accessor.messageHeaders)
    }

}