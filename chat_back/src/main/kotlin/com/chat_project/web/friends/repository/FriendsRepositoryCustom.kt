package com.chat_project.web.friends.repository

import com.chat_project.web.friends.dto.FriendsDTO
import com.chat_project.web.friends.dto.FriendsRequestDTO
import com.chat_project.web.friends.entity.Friends
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface FriendsRepositoryCustom {
    fun findFriendsRequestList(receiverId: Long): MutableList<FriendsRequestDTO>
    fun findFriendsRequest(receiverId: Long, senderId: Long): Friends?

    fun findFriendsList(pageable: Pageable, memberId: Long): Page<FriendsDTO>
}