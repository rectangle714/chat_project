package com.chat_project.web.chat.repository.file

import com.chat_project.common.util.logger
import com.chat_project.exception.CustomException
import com.chat_project.exception.CustomExceptionCode
import com.chat_project.security.TokenProvider
import com.chat_project.web.chat.dto.ChatRequestDTO
import com.chat_project.web.chat.dto.FileDTO
import com.chat_project.web.chat.repository.chat.ChatRepository
import com.chat_project.web.chat.repository.chatRoom.ChatRoomRepository
import com.chat_project.web.chat.repository.chatRoomMate.ChatRoomMemberRepository
import com.chat_project.web.chat.service.ChatRoomService
import com.chat_project.web.member.repository.MemberRepository
import com.chat_project.web.member.service.MemberService
import org.modelmapper.ModelMapper
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.listener.ChannelTopic
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import java.util.logging.Logger

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
    private val logger = logger()

    init {
        java.nio.file.Files.createDirectories(uploadDir)
    }

    fun fileUpload(chatRequestDTO: ChatRequestDTO?) {
        val fileDTO: FileDTO? = chatRequestDTO?.file

        if(fileDTO != null) {
            val user = tokenProvider.parseTokenInfo(chatRequestDTO.accessToken)
            val originFileName = fileDTO.fileName ?: "unknown"
            val storedFileName = generateStoredFileName(originFileName)
            val extension = originFileName.substringAfterLast('.', "")

            try {
                val decodedInputStream = fileDTO.fileData?.let {
                    ByteArrayInputStream(Base64.getDecoder().decode(it))
                } ?: throw CustomException(CustomExceptionCode.BAD_FILE_INFO)

                Files.newOutputStream(uploadDir.resolve(storedFileName)).use { outputStream ->
                    decodedInputStream.copyTo(outputStream)
                }
            } catch (error:Exception) {
                logger.error("파일 저장중 에러 발생 ${error.message}")
            }

        } else {
            throw CustomException(CustomExceptionCode.BAD_FILE_INFO)
        }
    }

    fun getFile(storedFileName: String): InputStream {
        val file: com.chat_project.web.chat.entity.Files = fileRepository.findByStoredFileName(storedFileName)
            ?: throw RuntimeException("File not found")

        return Files.newInputStream(uploadDir.resolve(file.storedFileName))
    }

    private fun generateStoredFileName(originFileName: String): String {
        return UUID.randomUUID().toString()
    }
}