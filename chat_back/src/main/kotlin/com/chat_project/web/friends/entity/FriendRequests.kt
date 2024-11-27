package com.chat_project.web.friends.entity

import com.chat_project.web.friends.enums.FriendRequestStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class FriendRequests(
    status: FriendRequestStatus?,
    senderId: Long,
    receiverId: Long
) {
    @Id @GeneratedValue
    @Column(name = "friend_requests_id")
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    val status: FriendRequestStatus = FriendRequestStatus.PENDING

    val senderId: Long? = null
    val receiverId: Long? = null
    val requestAt:LocalDateTime = LocalDateTime.now()

}