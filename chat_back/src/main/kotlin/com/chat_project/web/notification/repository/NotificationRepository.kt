package com.chat_project.web.notification.repository

import com.chat_project.web.notification.entity.Notification
import com.chat_project.web.notification.enums.NotiStatus
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository: JpaRepository<Notification, Long> {
    fun findByMemberIdAndStatus(memberId: Long, status: NotiStatus): MutableList<Notification>
}