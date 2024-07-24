package com.chat_project.web.chat.repository.chat

import com.chat_project.web.chat.dto.ChatResponseDTO
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.modelmapper.ModelMapper
import com.chat_project.web.chat.entity.QChat.chat
import com.chat_project.web.chat.entity.QChatRoom.chatRoom
import com.chat_project.web.member.entity.QMember.member

class ChatRepositoryImpl(
    private val query: JPAQueryFactory,
    private val modelMapper: ModelMapper
): ChatRepositoryCustom {
    /* 채팅방의 채팅 리스트 조회 */
    override fun findChattingList(chatRoomId: Long): MutableList<ChatResponseDTO> {
        return query
            .select(
                Projections.fields(
                    ChatResponseDTO::class.java,
                    chat.id.`as`("chatId"),
                    chat.message,
                    chat.registerDate,
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