package com.chat_project.config

import org.aopalliance.intercept.MethodInvocation
import org.springframework.aop.support.DelegatingIntroductionInterceptor
import org.springframework.web.socket.WebSocketSession

class SubProtocolWebSocketHandlerInterceptor(): DelegatingIntroductionInterceptor() {
    override fun doProceed(mi: MethodInvocation): Any? {
        if(mi.method.name.equals("afterConnectionEstablished") && mi.arguments.isNotEmpty()) {
            val session = mi.arguments[0] as? WebSocketSession
            session?.textMessageSizeLimit = 50 * 1024 * 1024
        }
        return super.doProceed(mi)
    }
}