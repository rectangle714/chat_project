package com.chat_project.common.util

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import java.lang.RuntimeException

object CommonUtil {
    fun getCurrentUserEmail(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        return if(authentication != null && authentication.principal is UserDetails) {
            (authentication.principal as UserDetails).username
        } else {
            throw RuntimeException("No authentication found")
        }
    }
}