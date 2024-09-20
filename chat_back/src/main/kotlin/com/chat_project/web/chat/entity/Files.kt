package com.chat_project.web.chat.entity

import com.chat_project.common.AuditableEntity
import com.chat_project.common.BaseEntity
import jakarta.persistence.*
import org.springframework.context.annotation.Description

@Entity(name = "File")
class Files(
    originFileName: String,
    storedFileName: String,
    fileType: String,
    fileSize: Long?,
    chat: Chat
): BaseEntity() {
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

    var fileType = fileType
        protected set

    @OneToOne
    @JoinColumn(name = "chat_id")
    var chat = chat
}