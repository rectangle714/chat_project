package com.chat_project.web.message

import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.dto.ChatDTO
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller

@Controller
class MessageController(
    private val tokenProvider: TokenProvider,
    private val memberRepository: MemberRepository,
    private val messageService: MessageService
    ) {
    @MessageMapping("/message")
    fun message(chatDTO: ChatDTO) {
        messageService.sendMessage(chatDTO)
    }
}