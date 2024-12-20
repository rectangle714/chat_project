package com.chat_project.web.message

import com.chat_project.common.util.logger
import com.chat_project.exception.CustomException
import com.chat_project.exception.CustomExceptionCode
import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.dto.ChatRequestDTO
import com.chat_project.web.chat.dto.ChatRoomResponseDTO
import com.chat_project.web.chat.entity.Chat
import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.chat.entity.ChatRoomMember
import com.chat_project.web.chat.entity.Files
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.chat.repository.chatRoom.ChatRoomRepository
import com.chat_project.web.chat.repository.chatRoomMate.ChatRoomMemberRepository
import com.chat_project.web.chat.repository.file.FileRepository
import com.chat_project.web.chat.repository.file.FileService
import com.chat_project.web.chat.service.ChatRoomService
import com.chat_project.web.chat.service.ChatService
import com.chat_project.web.member.dto.MemberDTO
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import com.chat_project.web.member.service.MemberService
import org.modelmapper.ModelMapper
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(rollbackFor = [ Exception::class ])
class MessageService(
    private val chatRepository: ChatRepository,
    private val chatRoomService: ChatRoomService,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomMemberRepository: ChatRoomMemberRepository,
    private val memberRepository: MemberRepository,
    private val memberService: MemberService,
    private val fileService: FileService,
    private val fileRepository: FileRepository,
    private val channelTopic: ChannelTopic,
    private val redisTemplate: StringRedisTemplate,
    private val modelMapper: ModelMapper,
    private val tokenProvider: TokenProvider
) {
    val logger = logger()

    fun sendMessage(chatRequestDTO: ChatRequestDTO) {
        val user = tokenProvider.parseTokenInfo(chatRequestDTO.accessToken)
        val member: Member =  user.username
            .let { memberRepository.findByEmail(it) }
            ?: throw CustomException(CustomExceptionCode.NOT_FOUND_MEMBER)
        val chatRoom: ChatRoom = chatRequestDTO.chatRoomId
            ?.let { chatRoomRepository.findById(it).get() }
            ?: throw CustomException(CustomExceptionCode.CHAT_ROOM_NOT_FOUND)
        chatRequestDTO.sender = member.nickname

        chatRequestDTO.file?.let {
            chatRequestDTO.isFile = "Y"
            chatRequestDTO.message = it.fileName.toString()

            val originFileName: String = it.fileName ?: "unknown"
            val storedFileName: String = fileService.generateStoredFileName(originFileName)
            it.storedFileName = storedFileName
            val extension: String = originFileName.substringAfterLast('.', "")

            val chat: Chat = chatRepository.save(Chat(it.fileName!!, member, chatRoom, isFile = "Y"))

            val file = Files(
                originFileName = originFileName,
                storedFileName = storedFileName,
                fileType = extension,
                fileSize = it.fileSize,
                chat = chat
            )
            fileRepository.save(file)

            chatRequestDTO.fileId = file.id
        } ?: run {
            val chat = chatRepository.save(Chat(chatRequestDTO.message, member, chatRoom, isFile = "N"))
        }

        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(ChatRequestDTO::class.java)
        redisTemplate.convertAndSend(channelTopic.topic, chatRequestDTO)
    }

    fun sendFile(chatRequestDTO: ChatRequestDTO) {
        sendMessage(chatRequestDTO)
        fileService.fileUpload(chatRequestDTO)
    }

    fun joinChatRoom(accessToken: String, roomId: Long) {
        val user = tokenProvider.parseTokenInfo(accessToken)
        var member: Member = user.username
            .let { memberRepository.findByEmail(it) }
            ?: run { throw CustomException(CustomExceptionCode.NOT_FOUND_MEMBER) }

        var chatRoom: ChatRoom? = null
        val memberDTO: MemberDTO = memberService.getMemberInfo(tokenProvider.parseTokenInfo(accessToken).username)
        var chatRoomDTO: ChatRoomResponseDTO

        roomId
            .let{
                chatRoom = chatRoomService.getChatRoomInfo(roomId.toString())
                    .orElseThrow { CustomException(CustomExceptionCode.CHAT_ROOM_NOT_FOUND) }
            }.run {
                chatRoomDTO = modelMapper.map(chatRoom, ChatRoomResponseDTO::class.java)
                chatRoomDTO
            }

        chatRoomMemberRepository.findByMemberIdAndChatRoomId(memberDTO.memberId, chatRoomDTO.id!!)
            ?: run {
                val member: Member = modelMapper.map(memberDTO, Member::class.java)
                chatRoomMemberRepository.save(ChatRoomMember(member, chatRoom!!))
                chatRepository.save(Chat("'${member.nickname}'님이 입장 했습니다.", member, chatRoom!!, "Y"))

                redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(ChatRequestDTO::class.java)
                redisTemplate.convertAndSend(
                    channelTopic.topic,
                    ChatRequestDTO(
                        chatRoomId = chatRoom!!.id,
                        memberId = member.id,
                        message= "'${member.nickname}'님이 입장 했습니다.",
                        sender = member.nickname,
                        alert = "Y"
                    )
                )
            }
    }

    fun exitChatRoom(accessToken: String, roomId: Long) {
        val user = tokenProvider.parseTokenInfo(accessToken)
        val member: Member = user.username
            .let { memberRepository.findByEmail(it) }
            ?: run { throw CustomException(CustomExceptionCode.NOT_FOUND_MEMBER) }
        val chatRoom: ChatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow { CustomException(CustomExceptionCode.CHAT_ROOM_NOT_FOUND) }

        chatRoomMemberRepository.findByMemberIdAndChatRoomId(member.id, chatRoom.id!!)
            ?. let {
                chatRoomMemberRepository.delete(it)
            }

        if(chatRoomMemberRepository.findByChatRoomId(chatRoom.id!!).isEmpty()) {
            chatRoomRepository.delete(chatRoom)
        } else {
            chatRepository.save(Chat("'${member.nickname}'님이 퇴장 했습니다.", member, chatRoom, "Y"))
            redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(ChatRequestDTO::class.java)
            redisTemplate.convertAndSend(
                channelTopic.topic, ChatRequestDTO(
                    chatRoomId = chatRoom.id,
                    memberId = member.id,
                    alert = "Y",
                    message= "'${member.nickname}'님이 퇴장 했습니다.",
                    sender = member.nickname,
                )
            )
        }
    }
}