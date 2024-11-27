package com.chat_project.web.chat.entity

import com.chat_project.common.AuditableEntity
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
class ChatRoom(
    roomName: String,
    numberPeople: Int
) : AuditableEntity() {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    var id:Long? = null

    @Comment("채팅방 이름")
    var roomName = roomName
        protected set

    @Comment("인원수")
    var numberPeople = numberPeople
        protected set

    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true)
    var chat:MutableList<Chat> = ArrayList()
        protected set

    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true)
    var chatRoomMembers: MutableList<ChatRoomMember> = mutableListOf()

    fun update(roomName:String, numberPeople: Int) {
        this.roomName = roomName
        this.numberPeople = numberPeople
    }
}