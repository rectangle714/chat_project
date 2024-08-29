package com.chat_project.web.message

import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.dto.ChatRequestDTO
import com.chat_project.web.chat.dto.ChatRoomRequestDTO
import com.chat_project.web.member.repository.MemberRepository
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
class MessageController(
    private val tokenProvider: TokenProvider,
    private val memberRepository: MemberRepository,
    private val messageService: MessageService
    ) {

    /* 메세지 전송 */
    @MessageMapping("/message")
    fun message(@Payload chatRequestDTO: ChatRequestDTO) {
        messageService.sendMessage(chatRequestDTO)
    }

    /* 채팅방 퇴장 */
    @MessageMapping("/chatRoom/exit/{roomId}")
    fun exitChatRoom(
            @Header("Authorization") accessToken: String,
            @DestinationVariable("roomId") roomId: Long,
            @Payload message: String
    ) {
        messageService.exitChatRoom(accessToken, roomId)
    }
}