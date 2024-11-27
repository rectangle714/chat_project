package com.chat_project.web.member

import com.chat_project.web.member.enums.Role
import com.chat_project.config.QuerydslConfig
import com.chat_project.web.TestBeanConfig
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import lombok.extern.slf4j.Slf4j
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.ContextConfiguration

@Slf4j
//@SpringBootTest
@DataJpaTest
@Import(QuerydslConfig::class)
@ContextConfiguration(classes = [TestBeanConfig::class])
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberTest {
    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Autowired
    private lateinit var chatRepository: ChatRepository

    @Autowired
    private lateinit var passwordEncoder: BCryptPasswordEncoder

    companion object {
        private const val TEST_ADMIN_EMAIL = "admin";
        private const val TEST_USER_EMAIL = "test";
    }

    @Test
    @Rollback(false)
    fun 사용자_생성() {
        memberRepository.save(
            Member("aa", passwordEncoder.encode("123"), "테스트1", Role.USER)
//            Member(TEST_ADMIN_EMAIL, passwordEncoder.encode("123"), "관리자", Role.ADMIN)
//                    Member(TEST_USER_EMAIL, passwordEncoder.encode("123"), "사용자", Role.USER)
        )
//        Assertions.assertNotNull(memberRepository.findByEmail(TEST_USER_EMAIL))
//        Assertions.assertNotNull(memberRepository.findByEmail(TEST_ADMIN_EMAIL))
    }

    @Test
    fun 사용자_조회() {
        Assertions.assertNotNull(memberRepository.findByEmail(TEST_USER_EMAIL))
    }
}