package com.chat_project.member

import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.common.constant.Role
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import lombok.extern.slf4j.Slf4j
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

@Slf4j
@SpringBootTest
class MemberTest {

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var chatRepository: ChatRepository

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    @Test
    @DisplayName("사용자 생성")
    @Transactional
    @Rollback(value = false)
    fun createMember() {
        val password: String = passwordEncoder.encode("123")
        val newMember: Member = Member("admin", password, "관리자", Role.ADMIN)
        memberRepository.save(newMember)
    }

    @Test
    @DisplayName("사용자 조회")
    @Transactional(readOnly = true)
    fun selectMember() {
        val findByEmail = memberRepository.findByEmail("test")
        println("사용자 정보 ${findByEmail?.email}");
    }

}