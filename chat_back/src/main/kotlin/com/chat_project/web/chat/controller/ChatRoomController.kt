package com.chat_project.web.chat.controller

import com.chat_project.web.chat.dto.ChatRoomRequestDTO
import com.chat_project.web.chat.dto.ChatRoomResponseDTO
import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.chat.service.ChatRoomService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.Optional

@RestController
@RequestMapping("/api/chatRoom")
@Tag(name = "채팅방 API", description = "채팅방 관련 전체 API" )
class ChatRoomController(
    private val chatRoomService: ChatRoomService
) {
    @GetMapping("/list")
    @Operation(method = "GET", summary = "채팅 목록 페이징 조회")
    fun pagingList(
        @PageableDefault(page = 0, size = 5) pageable:Pageable
    ): ResponseEntity<Page<ChatRoomResponseDTO>> {
        return ResponseEntity.ok(chatRoomService.getChatRoomListPaging(pageable))
    }

    @GetMapping("/{chatRoomId}")
    @Operation(method = "GET", summary = "채팅 목록 페이징 조회")
    fun getChatRoomInfo(@PathVariable chatRoomId: String): ResponseEntity<Optional<ChatRoom>> {
        return ResponseEntity.ok(chatRoomService.getChatRoomInfo(chatRoomId))
    }

    @PostMapping("/add")
    @Operation(method = "POST", summary = "채팅방 추가")
    fun add(chatRoomDTO: ChatRoomRequestDTO): ResponseEntity<String>
        = ResponseEntity.ok(chatRoomService.addChatRoom(chatRoomDTO))

    @PostMapping("/update")
    @Operation(method = "POST", summary = "채팅방 정보 변경")
    fun update(chatRoomDTO: ChatRoomRequestDTO): ResponseEntity<String>
            = ResponseEntity.ok(chatRoomService.updateChatRoom(chatRoomDTO))

    @PostMapping("/delete")
    @Operation(method = "POST", summary = "채팅방 정보 삭제")
    fun delete(chatRoomDTO: ChatRoomRequestDTO): ResponseEntity<String>
        = ResponseEntity.ok(chatRoomService.deleteChatRoom(chatRoomDTO))
}