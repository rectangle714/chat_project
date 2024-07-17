package com.chat_project.web.chat.controller

import com.chat_project.web.chat.dto.ChatRoomDTO
import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.chat.service.ChatRoomService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chatRoom")
@Tag(name = "채팅방 API", description = "채팅방 관련 전체 API" )
class ChatRoomController(
    private val chatRoomService: ChatRoomService
) {
    @GetMapping("/list")
    @Operation(method = "GET", summary = "채팅방 목록 조회")
    fun list(): ResponseEntity<MutableList<ChatRoom>>
        = ResponseEntity.ok(chatRoomService.getChatRoomList())

    @PostMapping("/add")
    @Operation(method = "POST", summary = "채팅방 추가")
    fun add(chatRoomDTO:ChatRoomDTO): ResponseEntity<String>
        = ResponseEntity.ok(chatRoomService.addChatRoom(chatRoomDTO))
}