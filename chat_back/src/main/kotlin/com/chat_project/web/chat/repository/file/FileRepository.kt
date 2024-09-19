package com.chat_project.web.chat.repository.file

import com.chat_project.web.chat.entity.Files
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository: JpaRepository<Files, Long> {
    fun findByStoredFileName(storedFileName: String): Files?
}