package com.chat_project.web.member.dto

import com.chat_project.common.constant.Role
import java.time.LocalDateTime

data class MemberDTO (
    var memberId: Long = 0,
    var email: String = "",
    var password: String = "",
    var nickname: String = "",
    var role: Role = Role.USER,
    var registerDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime = LocalDateTime.now()
)