package com.chat_project.web.friends.service

import io.mockk.verify
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class FriendsServiceTest {

    private lateinit var friendsService: FriendsServiceTest

    @Test
    @Transactional
    fun 친구추가테스트() {
        // given
        verify {  }


    }

}