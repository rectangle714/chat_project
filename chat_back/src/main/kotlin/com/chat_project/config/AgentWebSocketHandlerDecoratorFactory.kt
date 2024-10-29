package com.chat_project.config

import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.AopUtils
import org.springframework.aop.support.DefaultIntroductionAdvisor
import org.springframework.aop.target.SingletonTargetSource
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory

@Configuration
class AgentWebSocketHandlerDecoratorFactory: WebSocketHandlerDecoratorFactory {
    override fun decorate(handler: WebSocketHandler): WebSocketHandler {
        val proxyFactory: ProxyFactory = ProxyFactory()
        proxyFactory.targetClass = AopUtils.getTargetClass(handler)
        proxyFactory.targetSource = SingletonTargetSource(handler)
        proxyFactory.addAdvice(SubProtocolWebSocketHandlerInterceptor())
        proxyFactory.isOptimize = true
        proxyFactory.isExposeProxy = true
        return proxyFactory.proxy as WebSocketHandler
    }

}