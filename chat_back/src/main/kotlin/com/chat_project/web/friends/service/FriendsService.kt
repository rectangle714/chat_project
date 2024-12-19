package com.chat_project.web.friends.service

import com.chat_project.common.util.CommonUtil
import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.chat.entity.ChatRoomMember
import com.chat_project.web.chat.enums.RoomType
import com.chat_project.web.chat.repository.chatRoom.ChatRoomRepository
import com.chat_project.web.chat.repository.chatRoomMate.ChatRoomMemberRepository
import com.chat_project.web.friends.dto.FriendsDTO
import com.chat_project.web.friends.dto.FriendsRequestDTO
import com.chat_project.web.friends.entity.Friends
import com.chat_project.web.friends.enums.FriendStatus
import com.chat_project.web.friends.repository.FriendsRepository
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import com.chat_project.web.notification.entity.Notification
import com.chat_project.web.notification.enums.NotiStatus
import com.chat_project.web.notification.enums.NotiType
import com.chat_project.web.notification.repository.NotificationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(rollbackFor = [ Exception::class ])
class FriendsService(
    private val memberRepository: MemberRepository,
    private val friendsRepository: FriendsRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomMemberRepository: ChatRoomMemberRepository,
    private val notificationRepository: NotificationRepository,
    private val messageTemplate: SimpMessageSendingOperations
) {

    @Transactional(readOnly = true)
    fun getFriendsList(pageable: Pageable): Page<FriendsDTO> {
        val member = memberRepository.findByEmail(CommonUtil.getCurrentUserEmail())

        member?.let {
            return friendsRepository.findFriendsList(pageable, it.id)
        } ?: run {
            return Page.empty<FriendsDTO>()
        }
    }

    @Transactional(readOnly = true)
    fun getFriendRequestList(): MutableList<FriendsRequestDTO> {
        var friendsList: MutableList<FriendsRequestDTO> = mutableListOf()
        val email = CommonUtil.getCurrentUserEmail()
        val member = memberRepository.findByEmail(email)

        member?.let {
            friendsList = friendsRepository.findFriendsRequestList(it.id)
        }

        return friendsList;
    }

    fun handleFriendRequest(receiverId: Long) {
        val senderEmail: String = CommonUtil.getCurrentUserEmail()
        val sender = memberRepository.findByEmail(senderEmail)
            ?: throw IllegalStateException("사용자를 찾을 수 없습니다.: $senderEmail")

        val friends = friendsRepository.findByReceiverIdAndSenderIdAndStatus(
            receiverId, sender.id, FriendStatus.REJECTED
        )

        if (friends != null) {
            friends.updateStatusPending(FriendStatus.PENDING)
            friendsRepository.save(friends)
        } else {
            friendsRepository.save(
                Friends(
                    status = FriendStatus.PENDING,
                    senderId = sender.id,
                    receiverId = receiverId
                )
            )
        }

        val receiver: Member = memberRepository.findById(receiverId)
            .orElseThrow( { throw RuntimeException("존재하지 않는 사용자 입니다.")})

        notificationRepository.save(
            Notification(
                "친구 신청 요청이 도착했습니다.",
                NotiStatus.UNREAD,
                NotiType.FRIENDS_REQUEST,
                receiver
            )
        )
        messageTemplate.convertAndSend("/sub/notification/${receiver.email}", "친구요청 알림")
    }

    @Transactional(readOnly = true)
    fun checkRequest(receiverId: Long): String {
        var requestStatus: String = ""

        val sender: Member? = memberRepository.findByEmail(CommonUtil.getCurrentUserEmail()) as Member?
        sender?.let {
            val friends: Friends? = friendsRepository.findFriendsRequest(senderId = it.id, receiverId = receiverId)
            friends?.status?.let {
                requestStatus = it.toString()
            } ?: run {
                requestStatus = ""
            }

        }

        return requestStatus
    }

    fun acceptRequest(senderId: Long) {
        var friends: Friends?
        var chatRoom: ChatRoom
        val receiver: Member? = memberRepository.findByEmail(CommonUtil.getCurrentUserEmail()) as Member?
        val sender: Member = memberRepository.findById(senderId).get()

        receiver?.let {
            chatRoom = chatRoomRepository.save(ChatRoom(
                roomName = it.nickname,
                numberPeople = 2,
                roomType = RoomType.PRIVATE
            ))

            val joinMember: List<ChatRoomMember> =
                listOf(
                    ChatRoomMember(receiver, chatRoom),
                    ChatRoomMember(sender, chatRoom)
                ).let {
                    chatRoomMemberRepository.saveAll(it)
                } ?: throw RuntimeException("ChatRoomMember 등록 중 에러 발생.")

            friendsRepository
                .findByReceiverIdAndSenderIdAndStatus(
                    senderId= senderId,
                    receiverId = it.id,
                    status = FriendStatus.PENDING
                ) ?. let {
                    it.updateStatusAccepted(FriendStatus.ACCEPTED, chatRoom)
                    friendsRepository.save(it)
                } ?: throw NullPointerException("존재하지 않는 사용자 입니다.")
        } ?: throw NullPointerException("요청자를 찾을 수 없습니다.")
    }

    fun rejectRequest(senderId: Long) {
        val sender: Member? = memberRepository.findByEmail(CommonUtil.getCurrentUserEmail()) as Member?
        var friends:Friends?

        sender?.let {
            friendsRepository
                .findByReceiverIdAndSenderIdAndStatus(
                    senderId= senderId,
                    receiverId = it.id,
                    status = FriendStatus.PENDING
                ) ?.let {
                    it.updateStatusAccepted(FriendStatus.REJECTED, null)
                    friendsRepository.save(it)
                } ?: throw  NullPointerException("존재하지 않는 사용자 입니다.")
            } ?: throw NullPointerException("요청자를 찾을 수 없습니다.")
    }
}