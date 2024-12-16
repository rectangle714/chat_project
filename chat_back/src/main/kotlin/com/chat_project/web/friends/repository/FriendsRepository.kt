package com.chat_project.web.friends.repository

import com.chat_project.web.friends.entity.Friends
import com.chat_project.web.friends.enums.FriendStatus
import org.springframework.data.jpa.repository.JpaRepository

interface FriendsRepository: JpaRepository<Friends, Long>, FriendsRepositoryCustom {
    fun findByReceiverIdAndSenderIdAndStatus(receiverId: Long, senderId: Long, status: FriendStatus): Friends?
    fun findFriendsByReceiverIdOrSenderIdAndStatus(receiverId: Long, senderId: Long, status: FriendStatus): MutableList<Friends>
}