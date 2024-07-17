package com.chat_project.common.dto

import com.chat_project.common.constant.ApiStatus

data class ApiResponse(
    val status: ApiStatus,
    val message: String?,
    val data: Any?
) {
    companion object {
        fun success(data: Any?): ApiResponse {
            return ApiResponse(ApiStatus.SUCCESS, null, data)
        }

        fun error(message: String?): ApiResponse {
            return ApiResponse(ApiStatus.ERROR, message, null)
        }
    }
}
