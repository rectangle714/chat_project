package com.chat_project.web.chat.entity

import jakarta.persistence.*

@Entity
class File(
    originFileName: String,
    storedFileName: String,
    fileSize: Long?,
    chat: Chat
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    var id:Long? = null

    var originFileName = originFileName
        protected set

    var storedFileName = storedFileName
        protected set

    var fileSize = fileSize
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id")
    var chat = chat
}