package com.chat_project.web.friends.entity

import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.friends.enums.FriendStatus
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import java.time.LocalDateTime

@Entity
class Friends(
    status: FriendStatus = FriendStatus.PENDING,
    senderId: Long,
    receiverId: Long,
    chatRoom: ChatRoom? = null
) {
    @Id @GeneratedValue
    @Column(name = "friend_id")
    var id: Long? = null
        protected set

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

    @OneToOne
    @JoinColumn(name = "chat_room_id")
    var chatRoom:ChatRoom? = chatRoom
        protected set

    fun updateStatusAccepted(status: FriendStatus, chatRoom: ChatRoom?) {
        this.status = status
        this.chatRoom = chatRoom
    }
}