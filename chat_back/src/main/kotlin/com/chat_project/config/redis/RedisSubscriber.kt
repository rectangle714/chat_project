package com.chat_project.config.redis

import com.chat_project.common.util.logger
import com.chat_project.web.chat.dto.ChatDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/*
* 메세지 구독 구현 클래스
*/
@Service
@Transactional(rollbackFor = [ Exception::class ])
class RedisSubscriber(
    val objectMapper: ObjectMapper,
    val redisTemplate: StringRedisTemplate,
    val messagingTemplate: SimpMessageSendingOperations
): MessageListener {
    val logger = logger()

    override fun onMessage(message: Message, pattern: ByteArray?) {
        try {
            val publishMessage: String = redisTemplate.stringSerializer.deserialize(message.body) as String
            val chatDTO: ChatDTO = objectMapper.readValue(publishMessage, ChatDTO::class.java)

            messagingTemplate.convertAndSend("/sub/chat/room/"+ chatDTO.chatRoomId,
                                                mapOf("sender" to chatDTO.sender, "message" to chatDTO.message))
        } catch(e: Exception) {
            logger.error("채팅 에러 발생 : {}",e.message)
        }
    }

}