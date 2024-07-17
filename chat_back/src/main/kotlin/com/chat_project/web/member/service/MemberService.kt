package com.chat_project.web.member.service

import com.chat_project.common.util.RedisUtil
import com.chat_project.common.util.logger
import com.chat_project.exception.CustomException
import com.chat_project.exception.CustomExceptionCode
import com.chat_project.security.TokenDTO
import com.chat_project.security.TokenProvider
import com.chat_project.security.TokenType
import com.chat_project.web.member.dto.MemberDTO
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.PropertySource
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
@Transactional(rollbackFor = [Exception::class])
@PropertySource("classpath:jwt.yml")
class MemberService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val modelMapper: ModelMapper,
    private val tokenProvider: TokenProvider,
    private val redisUtil: RedisUtil
) {
    val logger = logger()

    @Transactional(readOnly = true)
    fun login(email: String, password: String): TokenDTO {
        val member: Member = memberRepository.findByEmail(email)
            .takeIf { passwordEncoder.matches(password, it?.password) }
            ?: throw IllegalArgumentException("아이디 또는 번호가 일치하지 않습니다.")

        val accessToken = tokenProvider.createToken("${member.email}:${member.role}", TokenType.ACCESS_TOKEN)
        val refreshToken = tokenProvider.createToken("${member.email}:${member.role}", TokenType.REFRESH_TOKEN)
        val accessTokenExpiration = tokenProvider.getTokenExpiration(accessToken)
        val refreshTokenExpiration = tokenProvider.getTokenExpiration(refreshToken)
        redisUtil.setData(member.email, refreshToken, refreshTokenExpiration)

        return TokenDTO(accessToken, refreshToken, accessTokenExpiration, refreshTokenExpiration)
    }

    fun logout(email: String, token: String): String {
        val parseToken: String = tokenProvider.parseBearerToken(token)!!
        val expiration = tokenProvider.getTokenExpiration(parseToken)
        redisUtil.getData(email).takeIf { redisUtil.deleteData(email) }
        redisUtil.setData(parseToken, "logout", Instant.ofEpochMilli(expiration).toEpochMilli())
        return "success"
    }

    fun reissue(refreshToken: String): TokenDTO {
        val tokenSubject: String = tokenProvider.getTokenSubject(refreshToken)!!
        val email: String = tokenSubject.split(":")[0]
        val redisRefreshToken = redisUtil.getData(email)
        if(Objects.isNull(redisRefreshToken) || !redisRefreshToken.equals(refreshToken.toString())) {
            throw CustomException(CustomExceptionCode.BAD_REFRESH_TOKEN_INFO)
        }

        val newToken = tokenProvider.createToken(tokenSubject, TokenType.ACCESS_TOKEN)
        val newTokenExpiration = tokenProvider.getTokenExpiration(newToken)
        return TokenDTO(newToken, "", newTokenExpiration, 0)
    }

    fun addMember(memberDTO: MemberDTO): String {
        memberRepository.save(Member.from(memberDTO, passwordEncoder))
        return "success"
    }

    fun updateMember(memberDTO: MemberDTO): String {
        val member: Member = memberRepository.findByEmail(memberDTO.email)
            .takeIf { passwordEncoder.matches(memberDTO.password, it?.password) }
            ?: throw CustomException(CustomExceptionCode.NOT_FOUND_MEMBER)
        member.update(memberDTO, passwordEncoder)
        memberRepository.flush()
        return "success"
    }

    @Transactional(readOnly = true)
    fun getMemberInfo(email: String): MemberDTO {
        val member: Member = memberRepository.findByEmail(email)
                                ?: throw CustomException(CustomExceptionCode.NOT_FOUND_MEMBER)
        return modelMapper.map(member, MemberDTO::class.java).also { it.password = "" }
    }

    fun deleteMember(email: String): String {
        val member: Member = memberRepository.findByEmail(email)
            ?: throw CustomException(CustomExceptionCode.NOT_FOUND_MEMBER)
        memberRepository.delete(member)
        return "success"
    }

}