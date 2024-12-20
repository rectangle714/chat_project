package com.chat_project.web.chat.entity

import com.chat_project.common.BaseEntity
import com.chat_project.web.member.entity.Member
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.springframework.context.annotation.Description

@Entity
class Chat(
    message: String,
    member: Member?,
    chatRoom: ChatRoom,
    isAlert: String = "N",
    isFile: String = "N"
): BaseEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    var id:Long? = null

    var message = message
        protected set

    @Comment("채팅 입장 OR 퇴장 알림 확인 컬럼")
    var isAlert = isAlert
        protected set

    @Comment("파일 채팅 조회용 컬럼")
    var isFile = isFile
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member = member

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    var chatRoom: ChatRoom = chatRoom

    @OneToOne(mappedBy = "chat", fetch = FetchType.LAZY)
    val file: Files? = null
}