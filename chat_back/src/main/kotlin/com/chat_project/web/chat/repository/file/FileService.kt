package com.chat_project.web.chat.repository.file

import com.chat_project.exception.CustomException
import com.chat_project.exception.CustomExceptionCode
import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.dto.ChatRequestDTO
import com.chat_project.web.chat.dto.FileDTO
import com.chat_project.web.chat.entity.Chat
import com.chat_project.web.chat.entity.File
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.chat.repository.chatRoom.ChatRoomRepository
import com.chat_project.web.chat.repository.chatRoomMate.ChatRoomMemberRepository
import com.chat_project.web.chat.service.ChatRoomService
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import com.chat_project.web.member.service.MemberService
import org.modelmapper.ModelMapper
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

@Service
class FileService(
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
    private val uploadDir: Path = Paths.get("uploads")

    init {
        Files.createDirectories(uploadDir)
    }

    fun fileUpload(chatRequestDTO: ChatRequestDTO?) {
        val fileDTO: FileDTO? = chatRequestDTO?.file
        if(fileDTO != null) {
            val user = tokenProvider.parseTokenInfo(chatRequestDTO.accessToken)

            val originFileName = fileDTO.fileName ?: "unknown"
            val storedFileName = generateStoredFileName(originFileName)

            // Base64로 인코딩된 파일 데이터 디코딩
            val decodedFileData = fileDTO.fileData?.let { Base64.getDecoder().decode(it) } ?: ByteArray(0)

            // 파일 시스템에 저장
            val filePath = uploadDir.resolve(storedFileName)
            Files.write(filePath, decodedFileData)

//            Chat()
//
//            val file: File = File(
//                originFileName = originFileName,
//                storedFileName = storedFileName,
//                fileType = fileDTO.fileType ?: "",
//                fileSize = fileDTO.fileSize ?: 0,
//                chat = Chat()
//            )
//
//            fileRepository.save(file)
        } else {
            throw CustomException(CustomExceptionCode.BAD_FILE_INFO)
        }
    }

    fun getFile(storedFileName: String): ByteArray {
        val file: File = fileRepository.findByStoredFileName(storedFileName)
            ?: throw RuntimeException("File not found")

        return Files.readAllBytes(uploadDir.resolve(file.storedFileName))
    }

    private fun generateStoredFileName(originFileName: String): String {
        val extension = originFileName.substringAfterLast('.', "")
        return "${System.currentTimeMillis()}_${originFileName}"
    }
}