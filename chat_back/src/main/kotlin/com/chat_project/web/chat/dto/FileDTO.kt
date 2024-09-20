package com.chat_project.web.chat.dto

data class FileDTO(
    val fileName: String?,
    val fileData: String?,
    val fileType: String?,
    val fileSize: Long?,
    var storedFileName: String?
)