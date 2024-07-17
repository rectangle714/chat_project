package com.chat_project.web.chat.repository.file

import com.chat_project.web.chat.entity.File
import org.springframework.data.jpa.repository.JpaRepository

interface FileRepository: JpaRepository<File, Long> {

}