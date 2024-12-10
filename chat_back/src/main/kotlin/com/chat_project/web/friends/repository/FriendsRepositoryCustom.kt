package com.chat_project.web.friends.repository

import com.chat_project.web.friends.dto.FriendsRequestDTO

interface FriendsRepositoryCustom {
    fun findFriendsRequestList(receiverId: Long): MutableList<FriendsRequestDTO>
}