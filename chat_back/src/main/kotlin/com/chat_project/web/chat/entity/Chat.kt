package com.chat_project.web.chat.entity

import com.chat_project.common.BaseEntity
import com.chat_project.web.member.entity.Member
import jakarta.persistence.*

@Entity
class Chat(
    message: String,
    member: Member?,
    chatRoom: ChatRoom
): BaseEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    var id:Long? = null

    var message = message
        protected set

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id")
    var member = member
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    var chatRoom: ChatRoom = chatRoom
}