package com.chat_project.common

import com.chat_project.web.security.CustomUserDetails
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuditorAwareImpl(): AuditorAware<String> {
    override fun getCurrentAuditor(): Optional<String> {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        return authentication?.let { Optional.of(it.name) } ?: Optional.empty()
    }
}