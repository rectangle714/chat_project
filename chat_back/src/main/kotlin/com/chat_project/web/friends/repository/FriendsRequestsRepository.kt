package com.chat_project.web.friends.repository

import com.chat_project.web.friends.entity.FriendRequests
import org.springframework.data.jpa.repository.JpaRepository

interface FriendsRequestsRepository: JpaRepository<FriendRequests, Long> {

}