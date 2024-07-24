package com.chat_project.web.message

import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.dto.ChatRequestDTO
import com.chat_project.web.member.repository.MemberRepository
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Controller

@Controller
class MessageController(
    private val tokenProvider: TokenProvider,
    private val memberRepository: MemberRepository,
    private val messageService: MessageService
    ) {
    @MessageMapping("/message")
    fun message(@Payload ChatRequestDTO: ChatRequestDTO) {
        messageService.sendMessage(ChatRequestDTO)
    }
}