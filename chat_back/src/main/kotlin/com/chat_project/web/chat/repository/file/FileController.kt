package com.chat_project.web.chat.repository.file

import com.chat_project.common.util.logger
import com.chat_project.web.chat.entity.Files
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.net.URLEncoder

@RestController
@RequestMapping("/api/file")
class FileController(
    private val fileRepository: FileRepository,
    private val fileService: FileService
) {
    val logger = logger()

    @GetMapping("/download/{fileId}")
    fun downloadFile(@PathVariable fileId: Long, response: HttpServletResponse) {
        val file: Files = fileService.getFile(fileId)

        // 파일 이름 및 경로 설정
        val fileName = file.originFileName
        val filePath = "src/main/resources/static/uploads/${file.storedFileName}"
        val fileToDownload = File(filePath)

        if (fileToDownload.exists()) {
            // HTTP 응답 설정
            response.contentType = "application/octet-stream"
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"))

            // 파일 스트리밍
            fileToDownload.inputStream().use { input ->
                input.copyTo(response.outputStream)
            }
        } else {
            throw Exception("File not found with id: $fileId")
        }
    }
}