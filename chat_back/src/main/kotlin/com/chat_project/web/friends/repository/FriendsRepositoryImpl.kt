package com.chat_project.web.friends.repository

import com.chat_project.web.friends.dto.FriendsRequestDTO
import com.querydsl.jpa.impl.JPAQueryFactory

import com.chat_project.web.member.entity.QMember.member
import com.chat_project.web.friends.entity.QFriends.friends
import com.chat_project.web.friends.enums.FriendStatus
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.DateTimeTemplate
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringTemplate
import java.time.LocalDateTime

class FriendsRepositoryImpl(
    private val query: JPAQueryFactory
): FriendsRepositoryCustom {
    override fun findFriendsRequestList(receiverId: Long): MutableList<FriendsRequestDTO> {
        val requestAt = Expressions.stringTemplate(
            "DATE_FORMAT({0}, '%Y-%m-%d %H:%i')",
            friends.requestAt
        )

        return query.select(Projections.bean(
            FriendsRequestDTO::class.java,
            member.id.`as`("senderId"),
            member.email.`as`("senderEmail"),
            friends.status,
            requestAt.`as`("requestAt")
        ))
            .from(friends)
            .leftJoin(member).on(friends.senderId.eq(member.id))
            .where(friends.receiverId.eq(receiverId).and(friends.status.eq(FriendStatus.PENDING)))
            .fetch()
    }
}