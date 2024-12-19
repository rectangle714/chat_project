package com.chat_project.web.notification.service

import com.chat_project.common.util.CommonUtil
import com.chat_project.web.member.repository.MemberRepository
import com.chat_project.web.notification.dto.NotificationResponseDTO
import com.chat_project.web.notification.entity.Notification
import com.chat_project.web.notification.enums.NotiStatus
import com.chat_project.web.notification.repository.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [Exception::class])
class NotificationService(
    private val memberRepository: MemberRepository,
    private val notificationRepository: NotificationRepository
) {
    fun getNotificationList(): MutableList<NotificationResponseDTO> {
        var notiDtoLIst: MutableList<NotificationResponseDTO> = mutableListOf()
        val member = memberRepository.findByEmail(CommonUtil.getCurrentUserEmail()) ?: throw RuntimeException("존재하지 않는 사용자 입니다.")

        notificationRepository.findByMemberIdAndStatus(member.id, NotiStatus.UNREAD)
            ?.let {
                it.forEach {
                    notiDtoLIst.add(NotificationResponseDTO(it.id))
                }
            }

        return notiDtoLIst;
    }

    fun updateStatusRead() {
        val member = memberRepository.findByEmail(CommonUtil.getCurrentUserEmail()) ?: throw RuntimeException("존재하지 않는 사용자 입니다.")
        val notiList = notificationRepository.findByMemberIdAndStatus(member.id, NotiStatus.UNREAD)

        for(noti in notiList) {
            noti.updateStatus(NotiStatus.READ)
        }

        notificationRepository.saveAll(notiList)
    }

}