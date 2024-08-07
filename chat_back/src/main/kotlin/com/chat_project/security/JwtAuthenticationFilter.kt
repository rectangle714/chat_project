package com.chat_project.security

import com.chat_project.common.util.RedisUtil
import com.chat_project.common.util.logger
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order
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
            if(null != request.getHeader("Authorization")) {
                val token = tokenProvider.parseBearerToken(request.getHeader("Authorization"));
                val user = tokenProvider.parseTokenInfo(token)
                if(Objects.nonNull(redisUtil.getData(user.username))) {
                    UsernamePasswordAuthenticationToken.authenticated(user, token, user.authorities)
                        .apply { details = WebAuthenticationDetails(request) }
                        .also { SecurityContextHolder.getContext().authentication = it }
                } else {
                    throw MalformedJwtException("Refresh 토큰 만료")
                }
            } else {
                throw MalformedJwtException("Access 토큰 만료")
            }
        } catch (e: Exception) {
            // 예외 발생 시 ExceptionHandler에서 처리한다.
            request.setAttribute("exception", e)
        }

        filterChain.doFilter(request, response)
    }
}