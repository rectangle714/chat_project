package com.chat_project.web.friends.controller

import io.swagger.v3.oas.annotations.Operation
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/friends")
class FriendsController {

    @PostMapping("/request")
    @Operation(method = "POST", summary = "친구 요청")
    fun friendRequest(receiverId: Long) {

    }

    @PostMapping("/add")
    @Operation(method = "POST", summary = "친구 추가")
    fun addFriend(memberId: Long) {

    }

}