package com.chat_project.web.message

import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.dto.ChatRequestDTO
import com.chat_project.web.chat.dto.ChatRoomRequestDTO
import com.chat_project.web.chat.dto.FileDTO
import com.chat_project.web.chat.repository.file.FileService
import com.chat_project.web.member.repository.MemberRepository
import jakarta.servlet.http.HttpServletResponse
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
class MessageController(
    private val tokenProvider: TokenProvider,
    private val memberRepository: MemberRepository,
    private val messageService: MessageService,
    private val fileService: FileService
    ) {

    /* 메세지 전송 */
    @MessageMapping("/message")
    fun message(@Payload chatRequestDTO: ChatRequestDTO)
        = messageService.sendMessage(chatRequestDTO)

    /* 파일 전송 */
    @MessageMapping("/sendFile")
    fun sendFile(@Payload chatRequestDTO: ChatRequestDTO)
        = messageService.sendFile(chatRequestDTO)

    /* 채팅방 입장 */
    @MessageMapping("/chatRoom/join/{roomId}")
    fun joinChatRoom(@Header("Authorization") accessToken: String, @DestinationVariable("roomId") roomId: Long)
        = messageService.joinChatRoom(accessToken, roomId)

    /* 채팅방 퇴장 */
    @MessageMapping("/chatRoom/exit/{roomId}")
    fun exitChatRoom(@Header("Authorization") accessToken: String, @DestinationVariable("roomId") roomId: Long)
        = messageService.exitChatRoom(accessToken, roomId)
}