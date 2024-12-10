package com.chat_project.web.friends.entity

import com.chat_project.web.friends.enums.FriendStatus
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.LocalDateTime

@Entity
class Friends(
    status: FriendStatus = FriendStatus.PENDING,
    senderId: Long,
    receiverId: Long
) {
    @Id @GeneratedValue
    @Column(name = "friend_id")
    var id: Long? = null

    @Enumerated(EnumType.STRING)
    var status: FriendStatus = status
        protected set

    @Comment("친구추가 요청한 사용자")
    var senderId: Long = senderId
        protected set

    @Comment("친구추가 받는 사용자")
    var receiverId: Long = receiverId
        protected set

    val requestAt:LocalDateTime = LocalDateTime.now()
}