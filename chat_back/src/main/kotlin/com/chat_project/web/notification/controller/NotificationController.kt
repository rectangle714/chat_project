package com.chat_project.web.notification.controller

import com.chat_project.web.notification.dto.NotificationResponseDTO
import com.chat_project.web.notification.entity.Notification
import com.chat_project.web.notification.service.NotificationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/notification")
@Tag(name = "알림 API", description = "알림 관련 전체 API")
class NotificationController(
    private val notificationService: NotificationService
) {
    @GetMapping("/list")
    @Operation(summary = "알림 리스트 조회")
    fun getNotificationList(): MutableList<NotificationResponseDTO> {
        return notificationService.getNotificationList()
    }

    @PostMapping("/updateStatusRead")
    @Operation(summary = "알림 상태 Read로 변경")
    fun updateStatusRead() {
        notificationService.updateStatusRead()
    }
}