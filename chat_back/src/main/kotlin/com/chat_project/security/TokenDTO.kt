package com.chat_project.security

data class TokenDTO(
    val accessToken: String = "",
    val refreshToken: String = "",
    val accessTokenExpiration: Long = 0,
    val refreshTokenExpiration: Long = 0
)
