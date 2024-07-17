package com.chat_project.web.member.repository

import com.chat_project.web.member.entity.Member

interface MemberRepositoryCustom {
    fun findByEmail(email: String): Member?
}