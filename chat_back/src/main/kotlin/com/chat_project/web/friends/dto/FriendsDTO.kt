package com.chat_project.web.friends.dto

data class FriendsDTO (
    var friendsId: Long = 0,
    var friendsEmail: String = "",
    var chatRoomId: Long = 0,
    var chatRoomName: String = ""
)