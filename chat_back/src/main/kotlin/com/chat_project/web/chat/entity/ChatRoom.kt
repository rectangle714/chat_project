package com.chat_project.web.chat.entity

import com.chat_project.common.AuditableEntity
import com.chat_project.web.chat.enums.RoomType
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
class ChatRoom(
    roomName: String,
    numberPeople: Int,
    roomType: RoomType = RoomType.PUBLIC
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

    @Comment("채팅창 구분(개인채팅, 오픈채팅)")
    var roomType: RoomType = roomType
        protected set

    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true)
    var chat:MutableList<Chat> = ArrayList()
        protected set

    @JsonIgnore
    @OneToMany(mappedBy = "chatRoom", cascade = [CascadeType.ALL], orphanRemoval = true)
    var chatRoomMembers: MutableList<ChatRoomMember> = mutableListOf()
        protected set

    fun update(roomName:String, numberPeople: Int) {
        this.roomName = roomName
        this.numberPeople = numberPeople
    }
}