package com.chat_project.web.notification.entity

import com.chat_project.common.BaseEntity
import com.chat_project.web.member.entity.Member
import com.chat_project.web.notification.enums.NotiStatus
import com.chat_project.web.notification.enums.NotiType
import jakarta.persistence.*

@Entity
class Notification(
    message: String,
    status: NotiStatus,
    type: NotiType,
    member: Member
): BaseEntity() {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    var id: Long = 0

    var message: String = message

    @Enumerated(EnumType.STRING)
    var status: NotiStatus = status

    @Enumerated(EnumType.STRING)
    var type: NotiType = type

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member = member

    fun updateStatus(notiStatus: NotiStatus) {
        this.status = notiStatus
    }
}