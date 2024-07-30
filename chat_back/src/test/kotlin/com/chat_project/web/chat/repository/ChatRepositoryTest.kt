package com.chat_project.web.chat.repository

import com.chat_project.config.QuerydslConfig
import com.chat_project.web.chat.dto.ChatRoomResponseDTO
import com.chat_project.web.chat.entity.Chat
import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.chat.repository.chatRoom.ChatRoomRepository
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import

@DataJpaTest
@Import(QuerydslConfig::class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatRepositoryTest(
    @Autowired private val memberRepository: MemberRepository,
    @Autowired private val chatRepository: ChatRepository,
    @Autowired private val chatRoomRepository: ChatRoomRepository,
    @Autowired private val modelMapper: ModelMapper
) {
    companion object {
        private const val TEST_EMAIL = "admin";
    }

    @Test
    fun 채팅입력() {
        val member: Member? = memberRepository.findByEmail(TEST_EMAIL)
        val chat: Chat? = member?.let { Chat( "testMessage", it, chatRoomRepository.findById(2).orElseGet(null)) }
        Assertions.assertNotNull(chat)
        chatRepository.save(chat);
    }

    @Test
    fun 채팅조회() {
        val member: Member? = memberRepository.findByEmail("test")
        val chatList: List<Chat>? = member?.let { chatRepository.findByMemberEmail(it.email) }
    }

    @Test
    fun 채팅방_생성() {
        chatRoomRepository.save(ChatRoom("테스트", 0))
    }

    @Test
    fun 채팅방_전체조회() {
        val chatroomList: List<ChatRoomResponseDTO> = chatRoomRepository.getChatRoomList()
        Assertions.assertEquals(chatroomList.size, 1);
    }

}