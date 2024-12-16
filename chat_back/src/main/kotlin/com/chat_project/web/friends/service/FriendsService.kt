package com.chat_project.web.friends.service

import com.chat_project.common.util.CommonUtil
import com.chat_project.security.TokenProvider
import com.chat_project.web.friends.dto.FriendsDTO
import com.chat_project.web.friends.dto.FriendsRequestDTO
import com.chat_project.web.friends.entity.Friends
import com.chat_project.web.friends.enums.FriendStatus
import com.chat_project.web.friends.repository.FriendsRepository
import com.chat_project.web.friends.repository.FriendsRepositoryCustom
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class FriendsService(
    private val memberRepository: MemberRepository,
    private val friendsRepository: FriendsRepository,
    private val tokenProvider: TokenProvider
) {

    fun getFriendsList(): MutableList<FriendsDTO> {
        val member = memberRepository.findByEmail(CommonUtil.getCurrentUserEmail())

        member?.let {
            return friendsRepository.findFriendsList(it.id)
        } ?: run {
            return mutableListOf()
        }
    }

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

        sender?.let {
            friendsRepository.save(Friends(status = FriendStatus.PENDING, senderId = it.id, receiverId = receiverId))
        }
    }

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
        val sender: Member? = memberRepository.findByEmail(CommonUtil.getCurrentUserEmail()) as Member?
        var friends:Friends?

        sender?.let {
            friends = friendsRepository
                .findByReceiverIdAndSenderIdAndStatus(
                    senderId= senderId,
                    receiverId = it.id,
                    status = FriendStatus.PENDING
                )

            friends?.let {
                it.status = FriendStatus.ACCEPTED
                friendsRepository.save(it)
            } ?: throw  NullPointerException("존재하지 않는 사용자 입니다.")
        } ?: throw NullPointerException("요청자를 찾을 수 없습니다.")
    }

    fun rejectRequest(senderId: Long) {
        val sender: Member? = memberRepository.findByEmail(CommonUtil.getCurrentUserEmail()) as Member?
        var friends:Friends?

        sender?.let {
            friends = friendsRepository
                .findByReceiverIdAndSenderIdAndStatus(
                    senderId= senderId,
                    receiverId = it.id,
                    status = FriendStatus.PENDING
                )

            friends?.let {
                it.status = FriendStatus.REJECTED
                friendsRepository.save(it)
            } ?: throw  NullPointerException("존재하지 않는 사용자 입니다.")
        } ?: throw NullPointerException("요청자를 찾을 수 없습니다.")
    }
}