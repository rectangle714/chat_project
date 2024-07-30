package com.chat_project.web.chat.repository.chatRoom

import com.chat_project.web.chat.dto.ChatRoomResponseDTO
import com.chat_project.web.chat.entity.QChat.chat
import com.chat_project.web.chat.entity.QChatRoom.chatRoom
import com.querydsl.core.Tuple
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.JPAExpressions.select
import com.querydsl.jpa.JPQLQuery
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.modelmapper.ModelMapper

class ChatRoomRepositoryImpl(
    private val query: JPAQueryFactory,
    private val modelMapper: ModelMapper
) : ChatRoomRepositoryCustom {
    override fun getChatRoomList(): MutableList<ChatRoomResponseDTO> {
        val lastMessageRegisterDateSubQuery = JPAExpressions
            .select(chat.registerDate.max())
            .from(chat)
            .where(chat.chatRoom.id.eq(chatRoom.id))

        return query
            .select(
                Projections.constructor(ChatRoomResponseDTO::class.java,
                    chatRoom.id,
                    chatRoom.roomName,
                    chatRoom.numberPeople,
                    chatRoom.registerDate,
                    chatRoom.updateDate,
                    chat.message.`as`("lastMessage")
                )
            )
            .from(chatRoom)
            .leftJoin(chat).on(chat.chatRoom.id.eq(chatRoom.id)
                .and(chat.registerDate.eq(lastMessageRegisterDateSubQuery)))
            .fetch()
    }
}