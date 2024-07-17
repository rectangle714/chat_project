package com.chat_project.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.sql.Date
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import javax.crypto.spec.SecretKeySpec

@Component
@PropertySource("classpath:jwt.yml")
class TokenProvider(
    @Value("\${secretKey}")
    private val secretKey: String,
    @Value("\${accessTokenExpiration}")
    private val accessTokenExpiration: Long,
    @Value("\${refreshTokenExpiration}")
    private val refreshTokenExpiration: Long,
) {
    fun createToken(userInfo: String, tokenType: TokenType): String {
        val expiration: Long = if(tokenType == TokenType.REFRESH_TOKEN) refreshTokenExpiration else accessTokenExpiration

        return Jwts.builder()
            .signWith(SecretKeySpec(secretKey.toByteArray(), SignatureAlgorithm.HS512.jcaName))
            .setSubject(userInfo)
            .setIssuedAt(Timestamp.valueOf(LocalDateTime.now()))
            .setExpiration(Date.from(Instant.now().plus(expiration, ChronoUnit.HOURS)))
            .compact()!!
    }

    fun getTokenSubject(token: String): String? = Jwts.parserBuilder()
        .setSigningKey(secretKey.toByteArray())
        .build()
        .parseClaimsJws(token)
        .body
        .subject

    fun getTokenExpiration(token: String?): Long {
        val expiration = Jwts.parserBuilder()
            .setSigningKey(secretKey.toByteArray())
            .build()
            .parseClaimsJws(token)
            .body
            .expiration

        return expiration.time - Instant.now().toEpochMilli()
    }

    fun parseBearerToken(token: String) =
        token.takeIf { it.startsWith("Bearer ", true) ?: false }?.substring(7)

    fun parseTokenInfo(token: String?) = (
            token?.takeIf { it.length >= 10 }
                ?.let { getTokenSubject(it) }
                ?: "anonymous:anonymous"
            ).split(":")
        .let { User(it[0], "", listOf(SimpleGrantedAuthority(it[1]))) }

}