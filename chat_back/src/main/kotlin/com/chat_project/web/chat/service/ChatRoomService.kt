package com.chat_project.web.chat.service

import com.chat_project.web.chat.dto.ChatRoomRequestDTO
import com.chat_project.web.chat.dto.ChatRoomResponseDTO
import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.chat.entity.ChatRoomMember
import com.chat_project.web.chat.repository.chatRoom.ChatRoomRepository
import com.chat_project.web.chat.repository.chatRoomMate.ChatRoomMemberRepository
import com.chat_project.web.member.dto.MemberDTO
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(rollbackFor = [ Exception::class ])
class ChatRoomService(
    private val modelMapper: ModelMapper,
    private val chatRoomRepository: ChatRoomRepository,
    private val memberRepository: MemberRepository,
    private val chatRoomMemberRepository: ChatRoomMemberRepository
) {
    @Transactional(readOnly = true)
    fun getChatRoomListPaging(pageable: Pageable): Page<ChatRoomResponseDTO> {
        return chatRoomRepository.getChatRoomList(pageable)
    }

    @Transactional(readOnly = true)
    fun getChatRoomInfo(roomId: String): Optional<ChatRoom> {
        return chatRoomRepository.findById(roomId.toLong());
    }

    fun addChatRoom(chatRoomDTO: ChatRoomRequestDTO): String {
        val chatRoom: ChatRoom = chatRoomRepository.save(modelMapper.map(chatRoomDTO, ChatRoom::class.java))

        val member: Member? = memberRepository.findByEmail(chatRoomDTO.email)
        val chatRoomMember: ChatRoomMember = ChatRoomMember(member, chatRoom)
        chatRoomMemberRepository.save(chatRoomMember)
        return "success"
    }

    fun updateChatRoom(chatRoomDTO: ChatRoomRequestDTO): String {
        val chatRoom: ChatRoom = chatRoomRepository.findById(chatRoomDTO.id)
            .orElseThrow { IllegalArgumentException("채팅방을 찾을 수 없습니다. ID: ${chatRoomDTO.id}") }
        chatRoom.update(chatRoomDTO.roomName!!, chatRoomDTO.numberPeople!!)
        return "success"
    }

    fun deleteChatRoom(chatRoomDTO: ChatRoomRequestDTO): String {
        val chatRoom: ChatRoom = chatRoomRepository.findById(chatRoomDTO.id)
            .orElseThrow { IllegalArgumentException("채팅방을 찾을 수 없습니다. ID: ${chatRoomDTO.id}") }
        chatRoomRepository.delete(chatRoom);
        return "success"
    }

    fun isUserInChatRoom(email:String, roomId: Long): String {
        var result: String = "fail"

        val memberDTO: MemberDTO = memberRepository.findByEmail(email)
            ?. let { modelMapper.map(it, MemberDTO::class.java) }
            ?: throw NoSuchElementException("찾을 수 없는 이메일 : $email")

        var chatRoom: ChatRoom;
        chatRoomRepository.findById(roomId)
            .let {
                chatRoom = it.orElseThrow {
                    IllegalArgumentException("존재하지 않는 방입니다.")
                }
            }

        chatRoomMemberRepository.findByMemberIdAndChatRoomId(memberDTO.memberId, roomId)
            ?. let {
                result = "success"
            }

        return result
    }

}