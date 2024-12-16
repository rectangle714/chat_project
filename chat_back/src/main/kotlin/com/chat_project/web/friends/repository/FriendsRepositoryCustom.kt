package com.chat_project.web.friends.repository

import com.chat_project.web.friends.dto.FriendsDTO
import com.chat_project.web.friends.dto.FriendsRequestDTO
import com.chat_project.web.friends.entity.Friends

interface FriendsRepositoryCustom {
    fun findFriendsRequestList(receiverId: Long): MutableList<FriendsRequestDTO>
    fun findFriendsRequest(receiverId: Long, senderId: Long): Friends?

    fun findFriendsList(memberId: Long): MutableList<FriendsDTO>
}