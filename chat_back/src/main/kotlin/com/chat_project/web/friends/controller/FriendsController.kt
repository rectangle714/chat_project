package com.chat_project.web.friends.controller

import com.chat_project.web.friends.service.FriendsService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/friends")
class FriendsController(
    private val friendsService: FriendsService
) {

    @PostMapping("/request")
    @Operation(method = "POST", summary = "친구 요청")
    fun friendRequest(@RequestParam(value = "receiverId") receiverId: Long) {
        friendsService.friendRequest(receiverId);
    }

    @PostMapping("/add")
    @Operation(method = "POST", summary = "친구 추가")
    fun addFriend(memberId: Long) {

    }

}