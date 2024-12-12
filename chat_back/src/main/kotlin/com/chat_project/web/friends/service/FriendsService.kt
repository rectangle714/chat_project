package com.chat_project.web.friends.service

import com.chat_project.common.util.CommonUtil
import com.chat_project.security.TokenProvider
import com.chat_project.web.friends.dto.FriendsRequestDTO
import com.chat_project.web.friends.entity.Friends
import com.chat_project.web.friends.enums.FriendStatus
import com.chat_project.web.friends.repository.FriendsRepository
import com.chat_project.web.friends.repository.FriendsRepositoryCustom
import com.chat_project.web.member.repository.MemberRepository
import org.springframework.stereotype.Service

@Service
class FriendsService(
    private val memberRepository: MemberRepository,
    private val friendsRepository: FriendsRepository,
    private val friendsRepositoryCustom: FriendsRepositoryCustom,
    private val tokenProvider: TokenProvider
) {

    fun getFriendRequestList(): MutableList<FriendsRequestDTO> {
        var friendsList: MutableList<FriendsRequestDTO> = mutableListOf()
        val email = CommonUtil.getCurrentUserEmail()
        val member = memberRepository.findByEmail(email)

        member?.let {
            friendsList = friendsRepositoryCustom.findFriendsRequestList(it.id)
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

}