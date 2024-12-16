package com.chat_project.web.friends.repository

import com.chat_project.config.QuerydslTestConfig
import com.chat_project.web.friends.entity.Friends
import com.chat_project.web.friends.enums.FriendStatus
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import javax.sql.DataSource

@DataJpaTest
@Import(QuerydslTestConfig::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FriendsRepositoryTest {

    @Autowired
    private lateinit var friendsRepository: FriendsRepository

    @Test
    fun 친구추가() {

        // given
        val receiverId: Long = 2;
        val senderId: Long = 1;
        val status: FriendStatus = FriendStatus.ACCEPTED

        // when
        val friends: Friends? = friendsRepository
            .findByReceiverIdAndSenderIdAndStatus(
                status = status,
                receiverId = receiverId,
                senderId = senderId
            )

        Assertions.assertNotNull(friends)

        // then
        friendsRepository.save(friends)


    }

}