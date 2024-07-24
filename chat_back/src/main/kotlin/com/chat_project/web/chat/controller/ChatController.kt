package com.chat_project.web.chat.controller

import com.chat_project.web.chat.dto.ChatResponseDTO
import com.chat_project.web.chat.service.ChatService
import com.chat_project.web.member.repository.MemberRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
@Tag(
    name = "채팅 API",
    description = "채팅 관련 전체 API ( 소켓 통신으로 작업 하는 내용 테스트 API )"
)
class ChatController(
    private val chatService: ChatService,
    private val memberRepository: MemberRepository
) {
    @GetMapping("/list")
    @Operation(method = "GET", summary = "채팅 목록 조회")
    fun list(chatRoomId: Long): MutableList<ChatResponseDTO>
        = chatService.getChatList(chatRoomId)

}