package com.chat_project.web.friends.controller

import com.chat_project.web.friends.dto.FriendsDTO
import com.chat_project.web.friends.dto.FriendsRequestDTO
import com.chat_project.web.friends.entity.Friends
import com.chat_project.web.friends.service.FriendsService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/friends")
class FriendsController(
    private val friendsService: FriendsService
) {

    @GetMapping("/list")
    @Operation(summary = "친구 목록 페이징 조회")
    fun getFriendsList(@PageableDefault(page = 0, size = 10) pageable: Pageable ): Page<FriendsDTO> {
        return friendsService.getFriendsList(pageable);
    }

    @GetMapping("/request/list")
    @Operation(summary = "친구 요청 목록")
    fun getFriendRequestList(): MutableList<FriendsRequestDTO> {
        return friendsService.getFriendRequestList();
    }

    @PostMapping("/request")
    @Operation(summary = "친구 요청")
    fun sendFriendRequest(@RequestParam(value = "receiverId") receiverId: Long) {
        friendsService.handleFriendRequest(receiverId);
    }

    @PostMapping("/request/{senderId}/accept")
    @Operation(summary = "친구 추가 수락")
    fun acceptRequest(@PathVariable("senderId") senderId: Long) {
        friendsService.acceptRequest(senderId)
    }

    @PostMapping("/request/{senderId}/reject")
    @Operation(summary = "친구 추가 거절")
    fun rejectRequest(@PathVariable("senderId") senderId: Long) {
        friendsService.rejectRequest(senderId)
    }

    @GetMapping("/request/check")
    @Operation(summary = "이미 친구요청을 보낸 사용자인지 체크")
    fun checkRequest(@RequestParam(value = "receiverId") receiverId: Long): Map<String, String> {
        val requestStatus = friendsService.checkRequest(receiverId);

        return mapOf("requestStatus" to requestStatus)
    }

}