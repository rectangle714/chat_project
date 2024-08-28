package com.chat_project.web.chat.entity

import com.chat_project.common.BaseEntity
import com.chat_project.web.member.entity.Member
import jakarta.persistence.*

@Entity
class ChatRoomMember(
    member:Member?,
    chatRoom:ChatRoom
): BaseEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_member_id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member? = member

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    var chatRoom: ChatRoom = chatRoom
}