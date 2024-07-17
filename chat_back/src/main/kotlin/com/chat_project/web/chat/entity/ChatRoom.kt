package com.chat_project.web.chat.entity

import com.chat_project.common.BaseEntity
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
class ChatRoom(
    roomName: String,
    numberPeople: Int
) : BaseEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    var id:Long? = null

    @Comment("채팅방 이름")
    var roomName = roomName
        protected set

    @Comment("인원수")
    var numberPeople = numberPeople
        protected set

    @OneToMany(mappedBy = "chatRoom")
    var chat:MutableList<Chat> = ArrayList()
        protected set
}