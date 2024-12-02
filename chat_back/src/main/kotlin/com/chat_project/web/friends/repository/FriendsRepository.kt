package com.chat_project.web.friends.repository

import com.chat_project.web.friends.entity.Friends
import org.springframework.data.jpa.repository.JpaRepository

interface FriendsRepository: JpaRepository<Friends, Long> {

}