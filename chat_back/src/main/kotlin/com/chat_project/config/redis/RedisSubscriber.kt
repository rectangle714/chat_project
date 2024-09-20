package com.chat_project.config.redis

import com.chat_project.common.util.logger
import com.chat_project.web.chat.dto.ChatRequestDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.connection.Message
import org.springframework.data.redis.connection.MessageListener
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
            val chatRequestDTO: ChatRequestDTO = objectMapper.readValue(publishMessage, ChatRequestDTO::class.java)
            val registerDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))

            var isFile: String = ""
            chatRequestDTO.file?.let { isFile = "Y" }

            messagingTemplate.convertAndSend("/sub/chat/room/"+ chatRequestDTO.chatRoomId,
                                                mapOf("sender" to chatRequestDTO.sender, "message" to chatRequestDTO.message
                                                        , "registerDate" to registerDate, "isAlert" to chatRequestDTO.alert
                                                        , "isFile" to isFile, "fileId" to chatRequestDTO.fileId))
        } catch(e: Exception) {
            logger.error("채팅 에러 발생 : {}",e.message)
        }
    }

}