package com.chat_project.security

import com.chat_project.common.util.logger
import com.chat_project.common.util.RedisUtil
import com.chat_project.exception.CustomException
import com.chat_project.exception.CustomExceptionCode
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Order(0)
@Component
class JwtAuthenticationFilter(
    private val tokenProvider: TokenProvider,
    private val redisUtil: RedisUtil
) : OncePerRequestFilter() {
    val logger = logger()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val token = tokenProvider.parseBearerToken(request.getHeader(HttpHeaders.AUTHORIZATION))
            val user = tokenProvider.parseTokenInfo(token)
            if(Objects.nonNull(redisUtil.getData(user.username))) {
                UsernamePasswordAuthenticationToken.authenticated(user, token, user.authorities)
                    .apply { details = WebAuthenticationDetails(request) }
                    .also { SecurityContextHolder.getContext().authentication = it }
            } else {
                throw CustomException(CustomExceptionCode.BAD_TOKEN_INFO)
            }
        } catch (e: Exception) {
            // 예외 발생 시 ExceptionHandler에서 처리한다.
            request.setAttribute("exception", e)
        }


        filterChain.doFilter(request, response)
    }
}