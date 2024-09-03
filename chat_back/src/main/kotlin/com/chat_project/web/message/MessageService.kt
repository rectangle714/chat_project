package com.chat_project.web.message

import com.chat_project.common.constant.ChatType
import com.chat_project.common.util.logger
import com.chat_project.exception.CustomException
import com.chat_project.exception.CustomExceptionCode
import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.dto.ChatRequestDTO
import com.chat_project.web.chat.dto.ChatRoomRequestDTO
import com.chat_project.web.chat.dto.ChatRoomResponseDTO
import com.chat_project.web.chat.dto.FileDTO
import com.chat_project.web.chat.entity.Chat
import com.chat_project.web.chat.entity.ChatRoom
import com.chat_project.web.chat.entity.ChatRoomMember
import com.chat_project.web.chat.entity.File
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.chat.repository.chatRoom.ChatRoomRepository
import com.chat_project.web.chat.repository.chatRoomMate.ChatRoomMemberRepository
import com.chat_project.web.chat.repository.file.FileRepository
import com.chat_project.web.chat.service.ChatRoomService
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
import org.springframework.web.multipart.MultipartFile
import java.lang.IllegalArgumentException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Base64

@Service
@Transactional(rollbackFor = [ Exception::class ])
class MessageService(
    private val chatRoomService: ChatRoomService,
    private val chatRepository: ChatRepository,
    private val chatRoomRepository: ChatRoomRepository,
    private val chatRoomMemberRepository: ChatRoomMemberRepository,
    private val memberRepository: MemberRepository,
    private val memberService: MemberService,
    private val fileRepository: FileRepository,
    private val channelTopic: ChannelTopic,
    private val redisTemplate: StringRedisTemplate,
    private val modelMapper: ModelMapper,
    private val tokenProvider: TokenProvider
) {
    val logger = logger()

    private val uploadDir: Path = Paths.get("uploads")

    init {
        Files.createDirectories(uploadDir)
    }


    fun sendMessage(ChatRequestDTO: ChatRequestDTO) {
        val user = tokenProvider.parseTokenInfo(ChatRequestDTO.accessToken)
        val member: Member =  user.username
                                .let { memberRepository.findByEmail(it) }
                                ?: throw CustomException(CustomExceptionCode.NOT_FOUND_MEMBER)
        val chatRoom: ChatRoom = ChatRequestDTO.chatRoomId
            ?.let { chatRoomRepository.findById(it).get() }
            ?: throw CustomException(CustomExceptionCode.CHAT_ROOM_NOT_FOUND)

        ChatRequestDTO.sender = member.nickname
        chatRepository.save(Chat(ChatRequestDTO.message, member, chatRoom))

        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(ChatRequestDTO::class.java)
        redisTemplate.convertAndSend(channelTopic.topic, ChatRequestDTO)
    }

    fun handleFileUpload(fileDTO: FileDTO?) {
        val originFileName = fileDTO?.fileName ?: "unknown"
        val fileData = Base64.getDecoder().decode(fileDTO?.fileData)
        val storedFileName = generateStoredFileName(originFileName)

        // Base64로 인코딩된 파일 데이터 디코딩
        val decodedFileData = fileDTO?.fileData?.let { Base64.getDecoder().decode(it) } ?: ByteArray(0)

        // 파일 시스템에 저장
        val filePath = uploadDir.resolve(storedFileName)
        Files.write(filePath, decodedFileData)

//        val file = File(
//            originFileName = originFileName,
//            storedFileName = storedFileName,
//            fileType = fileDTO?.fileType ?: "",
//            fileSize = fileDTO?.fileSize ?: 0,
//            chat = Chat()
//        )
//        fileRepository.save()
    }

    fun getFile(storedFileName: String): ByteArray {
        val fileEntity = fileRepository.findByStoredFileName(storedFileName)
            ?: throw RuntimeException("File not found")

        return Files.readAllBytes(uploadDir.resolve(fileEntity.storedFileName))
    }

    private fun generateStoredFileName(originFileName: String): String {
        val extension = originFileName.substringAfterLast('.', "")
        return "${System.currentTimeMillis()}_${originFileName}"
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