package com.chat_project.web.chat.repository.chat

import com.chat_project.web.chat.dto.ChatResponseDTO
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.modelmapper.ModelMapper
import com.chat_project.web.chat.entity.QChat.chat
import com.chat_project.web.chat.entity.QChatRoom.chatRoom
import com.chat_project.web.member.entity.QMember.member
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.core.types.dsl.StringPath
import com.querydsl.core.types.dsl.StringTemplate

class ChatRepositoryImpl(
    private val query: JPAQueryFactory
): ChatRepositoryCustom {
    /* 채팅방의 채팅 리스트 조회 */
    override fun findChattingList(chatRoomId: Long): MutableList<ChatResponseDTO> {
        val registerDate: StringTemplate = Expressions.stringTemplate(
            "DATE_FORMAT({0}, '%H:%i')",
            chat.registerDate
        );

        return query
            .select(
                Projections.fields(
                    ChatResponseDTO::class.java,
                    chat.id.`as`("chatId"),
                    chat.message,
                    registerDate.`as`("registerDate"),
                    chatRoom.id.`as`("chatRoomId"),
                    member.nickname.`as`("sender"),
                )
            )
            .from(chat)
            .join(chat.chatRoom, chatRoom)
            .join(chat.member, member)
            .where(chat.chatRoom.id.eq(chatRoomId))
            .orderBy(chat.registerDate.asc())
            .fetch()
    }
}