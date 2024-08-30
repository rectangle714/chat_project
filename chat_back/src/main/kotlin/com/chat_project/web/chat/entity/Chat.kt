package com.chat_project.web.chat.entity

import com.chat_project.common.BaseEntity
import com.chat_project.web.member.entity.Member
import jakarta.persistence.*

@Entity
class Chat(
    message: String,
    member: Member?,
    chatRoom: ChatRoom,
    isAlert: String = "N"
): BaseEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    var id:Long? = null

    var message = message
        protected set

    var isAlert = isAlert
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member = member

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    var chatRoom: ChatRoom = chatRoom

}