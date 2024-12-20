package com.chat_project.web.member.dto

import com.chat_project.web.member.enums.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class MemberDTO (
    var memberId: Long = 0,
    var email: String = "",
    @JsonIgnore
    var password: String = "",
    var nickname: String = "",
    var role: Role = Role.USER,
    var registerDate: LocalDateTime = LocalDateTime.now(),
    var updateDate: LocalDateTime = LocalDateTime.now()
)