package com.chat_project.web

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.domain.AuditorAware
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@TestConfiguration
class TestBeanConfig {
    @Bean
    fun auditorProvider(): AuditorAware<String> {
        return AuditorAware { Optional.of("testUser") }
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

}