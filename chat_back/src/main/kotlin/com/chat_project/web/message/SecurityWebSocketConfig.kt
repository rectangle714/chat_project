package com.chat_project.web.message

import com.fasterxml.jackson.core.StreamReadConstraints
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity
import org.springframework.security.messaging.access.intercept.AuthorizationChannelInterceptor
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager
import org.springframework.security.messaging.util.matcher.MessageMatcher

@Configuration
@EnableWebSocketSecurity
class SecurityWebSocketConfig {
    @Bean
    fun authorizationManager(messages: MessageMatcherDelegatingAuthorizationManager.Builder): AuthorizationManager<Message<*>> {
        messages
            .nullDestMatcher().permitAll()
            .simpTypeMatchers(SimpMessageType.CONNECT).permitAll()
            .simpTypeMatchers(SimpMessageType.SUBSCRIBE).permitAll()
            .simpTypeMatchers(SimpMessageType.MESSAGE).permitAll()
            .anyMessage().denyAll()

        return messages.build()
    }

}