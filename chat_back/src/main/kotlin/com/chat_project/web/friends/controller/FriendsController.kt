package com.chat_project.web.friends.controller

import com.chat_project.web.friends.dto.FriendsRequestDTO
import com.chat_project.web.friends.entity.Friends
import com.chat_project.web.friends.service.FriendsService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/friends")
class FriendsController(
    private val friendsService: FriendsService
) {
    @GetMapping("/requests/list")
    @Operation(summary = "친구 요청 목록")
    fun getFriendRequestList(): MutableList<FriendsRequestDTO> {
        return friendsService.getFriendRequestList();
    }

    @PostMapping("/request")
    @Operation(summary = "친구 요청")
    fun sendFriendRequest(@RequestParam(value = "receiverId") receiverId: Long) {
        friendsService.handleFriendRequest(receiverId);
    }

    @PostMapping("/add")
    @Operation(summary = "친구 추가")
    fun addFriend(memberId: Long) {

    }

}