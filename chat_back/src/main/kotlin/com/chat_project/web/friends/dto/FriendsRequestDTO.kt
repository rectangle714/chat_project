package com.chat_project.web.friends.dto

import com.chat_project.web.friends.enums.FriendStatus
import java.time.LocalDateTime

data class FriendsRequestDTO(
    var senderId: Long = 0,
    var senderEmail: String = "",
    var status: FriendStatus = FriendStatus.PENDING,
    var requestAt: String = ""
)