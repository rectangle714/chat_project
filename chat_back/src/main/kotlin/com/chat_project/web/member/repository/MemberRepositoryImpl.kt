package com.chat_project.web.member.repository

import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.entity.QMember.member
import com.querydsl.jpa.impl.JPAQueryFactory

class MemberRepositoryImpl(
    private val query: JPAQueryFactory
) : MemberRepositoryCustom {
    override fun findByEmail(email: String): Member? {
        return query
            .selectFrom(member)
            .where(member.email.eq(email))
            .fetchOne();
    }
}