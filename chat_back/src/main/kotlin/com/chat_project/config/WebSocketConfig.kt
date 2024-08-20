package com.chat_project.config

import com.chat_project.config.redis.StompErrorHandler
import com.chat_project.config.redis.StompHandler
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.security.authorization.AuthenticatedAuthorizationManager
import org.springframework.security.authorization.AuthorizationEventPublisher
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.authorization.SpringAuthorizationEventPublisher
import org.springframework.security.messaging.access.intercept.AuthorizationChannelInterceptor
import org.springframework.security.messaging.context.AuthenticationPrincipalArgumentResolver
import org.springframework.security.messaging.context.SecurityContextChannelInterceptor
import org.springframework.web.socket.config.annotation.*

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val stompHandler: StompHandler,
    private val stompErrorHandler: StompErrorHandler
): WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
        registry.setErrorHandler(stompErrorHandler)
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/sub")
        registry.setApplicationDestinationPrefixes("/pub")
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        var myAuthorizationRules: AuthorizationManager<Message<*>> = AuthenticatedAuthorizationManager.authenticated()
        var authz: AuthorizationChannelInterceptor = AuthorizationChannelInterceptor(myAuthorizationRules)
        registration.interceptors(SecurityContextChannelInterceptor(), authz)
        registration.interceptors(stompHandler)
    }

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        argumentResolvers.add(AuthenticationPrincipalArgumentResolver())
    }
}