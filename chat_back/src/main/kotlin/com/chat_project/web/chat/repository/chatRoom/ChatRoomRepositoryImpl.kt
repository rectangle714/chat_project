package com.chat_project.web.chat.repository.chatRoom

import com.chat_project.web.chat.dto.ChatRoomResponseDTO
import com.chat_project.web.chat.entity.QChat.chat
import com.chat_project.web.chat.entity.QChatRoom.chatRoom
import com.chat_project.web.chat.entity.QChatRoomMember.chatRoomMember
import com.chat_project.web.chat.enums.RoomType
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable

class ChatRoomRepositoryImpl(
    private val query: JPAQueryFactory
) : ChatRoomRepositoryCustom {
    override fun getChatRoomList(pageable: Pageable, roomType: String): Page<ChatRoomResponseDTO> {
        val lastMessageRegisterDateSubQuery = JPAExpressions
            .select(chat.registerDate.max())
            .from(chat)
            .where(chat.chatRoom.id.eq(chatRoom.id).and(chat.isAlert.ne("Y")))

        val memberCountSubQuery = JPAExpressions
            .select(chatRoomMember.count())
            .from(chatRoomMember)
            .where(chatRoomMember.chatRoom.id.eq(chatRoom.id))

        val roomTypeCondition =
            when(roomType) {
                "private" -> chatRoom.roomType.eq(RoomType.PRIVATE)
                else -> chatRoom.roomType.eq(RoomType.PUBLIC)
            }


        val chatRoomList: MutableList<ChatRoomResponseDTO> = query
            .select(
                Projections.constructor(
                    ChatRoomResponseDTO::class.java,
                    chatRoom.id,
                    chatRoom.roomName,
                    chatRoom.numberPeople,
                    chatRoom.registerDate,
                    chatRoom.updateDate,
                    chat.message.`as`("lastMessage"),
                    chat.registerDate.`as`("lastSendDate"),
                    memberCountSubQuery
                )
            )
            .from(chatRoom)
            .leftJoin(chat).on(
                chat.chatRoom.id.eq(chatRoom.id)
                    .and(chat.registerDate.eq(lastMessageRegisterDateSubQuery))
            )
            .where(roomTypeCondition)
            .orderBy(chatRoom.registerDate.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val totalCount: Long = query
            .select(
                Projections.constructor(
                    ChatRoomResponseDTO::class.java,
                    chatRoom.id,
                    chatRoom.roomName,
                    chatRoom.numberPeople,
                    chatRoom.registerDate,
                    chatRoom.updateDate,
                    chat.message.`as`("lastMessage"),
                    chat.registerDate.`as`("lastSendDate"),
                    memberCountSubQuery
                )
            )
            .from(chatRoom)
            .leftJoin(chat).on(
                chat.chatRoom.id.eq(chatRoom.id)
                    .and(chat.registerDate.eq(lastMessageRegisterDateSubQuery))
            )
            .fetch().size.toLong()

        return PageImpl(chatRoomList, pageable, totalCount)
    }
}