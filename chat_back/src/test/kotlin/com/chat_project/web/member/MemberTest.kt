package com.chat_project.web.member

import com.chat_project.common.constant.Role
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import lombok.extern.slf4j.Slf4j
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Slf4j
@SpringBootTest
class MemberTest {
    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var chatRepository: ChatRepository

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    companion object {
        private const val TEST_EMAIL = "admin";
    }

    @Test
    fun 사용자_생성() {
        memberRepository.save(
            Member(TEST_EMAIL, passwordEncoder.encode("123"), "관리자", Role.ADMIN)
        )
        Assertions.assertNotNull(memberRepository.findByEmail(TEST_EMAIL))
    }

    @Test
    fun 사용자_조회() {
        Assertions.assertNotNull(memberRepository.findByEmail(TEST_EMAIL))
    }
}