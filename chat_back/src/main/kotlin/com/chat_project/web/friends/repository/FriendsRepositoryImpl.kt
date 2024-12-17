package com.chat_project.web.friends.repository

import com.chat_project.web.friends.dto.FriendsDTO
import com.chat_project.web.friends.dto.FriendsRequestDTO
import com.chat_project.web.friends.entity.Friends
import com.querydsl.jpa.impl.JPAQueryFactory

import com.chat_project.web.member.entity.QMember.member
import com.chat_project.web.friends.entity.QFriends.friends
import com.chat_project.web.friends.enums.FriendStatus
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.DateTimeTemplate
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringTemplate
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
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

    override fun findFriendsRequest(receiverId: Long, senderId: Long): Friends? {
        return query.selectFrom(friends)
            .where(
                (friends.senderId.eq(senderId).and(friends.receiverId.eq(receiverId)))
            )
            .fetchOne()
    }

    override fun findFriendsList(pageable: Pageable , memberId: Long): Page<FriendsDTO> {

        val friendsList: MutableList<FriendsDTO> = query
            .select(
                Projections.bean(
                    FriendsDTO::class.java,
                    Expressions.cases()
                        .`when`(friends.senderId.eq(memberId)).then(friends.receiverId)
                        .`when`(friends.receiverId.eq(memberId)).then(friends.senderId)
                        .otherwise(-1L).`as`("friendsId"),
                    Expressions.cases()
                        .`when`(friends.senderId.eq(memberId)).then(friends.receiverId)
                        .`when`(friends.receiverId.eq(memberId)).then(friends.senderId)
                        .otherwise(-1L).`as`("friendsId"),
                    member.email.`as`("friendsEmail"),
                    member.nickname.`as`("chatRoomName"),
                    friends.chatRoom.id.`as`("chatRoomId")
                )
            )
            .from(friends)
            .join(member)
            .on(
                (friends.senderId.eq(memberId).and(member.id.eq(friends.receiverId)))
                    .or(friends.receiverId.eq(memberId).and(member.id.eq(friends.senderId)))
            )
            .where(friends.status.eq(FriendStatus.ACCEPTED))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val totalCount: Long = query
            .select(
                Projections.bean(
                    FriendsDTO::class.java,
                    Expressions.cases()
                        .`when`(friends.senderId.eq(memberId)).then(friends.receiverId)
                        .`when`(friends.receiverId.eq(memberId)).then(friends.senderId)
                        .otherwise(-1L).`as`("friendsId"),
                    member.email.`as`("friendsEmail")
                )
            )
            .from(friends)
            .join(member)
            .on(
                (friends.senderId.eq(memberId).and(member.id.eq(friends.receiverId)))
                    .or(friends.receiverId.eq(memberId).and(member.id.eq(friends.senderId)))
            )
            .where(friends.status.eq(FriendStatus.ACCEPTED))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch().size.toLong()

        return PageImpl(friendsList, pageable, totalCount)
    }
}