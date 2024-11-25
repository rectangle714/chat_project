package com.chat_project.config

import com.chat_project.config.redis.StompErrorHandler
import com.chat_project.config.redis.StompHandler
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.*

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig(
    private val stompHandler: StompHandler,
    private val stompErrorHandler: StompErrorHandler,
    private val agentWebSocketHandlerDecoratorFactory: AgentWebSocketHandlerDecoratorFactory
): WebSocketMessageBrokerConfigurer {
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry
            .addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS()
        registry.setErrorHandler(stompErrorHandler)
    }

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/sub")
        registry.setApplicationDestinationPrefixes("/pub")
    }

    override fun configureWebSocketTransport(registry: WebSocketTransportRegistration) {
        registry.setDecoratorFactories(agentWebSocketHandlerDecoratorFactory)
        registry.setMessageSizeLimit(160 * 64 * 1024)
        registry.setSendTimeLimit(100 * 10000)
        registry.setSendBufferSizeLimit(3 * 512 * 1024)
    }

    override fun configureClientInboundChannel(registration: ChannelRegistration) {
        registration.interceptors(stompHandler)
    }
}