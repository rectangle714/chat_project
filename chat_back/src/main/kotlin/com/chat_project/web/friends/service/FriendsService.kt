package com.chat_project.web.friends.service

import com.chat_project.security.TokenProvider
import com.chat_project.web.friends.repository.FriendsRequestsRepository
import org.springframework.boot.autoconfigure.security.SecurityProperties.User
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.security.Principal

@Service
class FriendsService(
    private val friendsRequestsRepository: FriendsRequestsRepository,
    private val tokenProvider: TokenProvider
) {
    fun friendRequest(receiverId: Long) {
        val authentication: Any? = SecurityContextHolder.getContext().authentication.principal

//        FriendRequests(receiverId = receiverId, status = FriendRequestStatus.PENDING, senderId = email)
    }

}