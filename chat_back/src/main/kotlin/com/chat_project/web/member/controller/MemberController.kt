package com.chat_project.web.member.controller

import com.chat_project.common.util.logger
import com.chat_project.exception.CustomExceptionCode
import com.chat_project.security.TokenDTO
import com.chat_project.web.member.dto.MemberDTO
import com.chat_project.web.member.entity.Member
import com.chat_project.web.member.repository.MemberRepository
import com.chat_project.web.member.service.MemberService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/member")
@Tag(name = "사용자 API", description = "사용자 관련 전체 API")
class MemberController(
    private val memberService: MemberService,
    private val memberRepository: MemberRepository,
    private val modelMapper: ModelMapper,
    private val passwordEncoder: BCryptPasswordEncoder
) {
    val log = logger()

    @Operation(method = "POST", summary = "사용자 회원가입", description = "사용자 회원가입 API")
    @PostMapping("/signup")
    fun signUp(memberDTO: MemberDTO) : ResponseEntity<String>
         = ResponseEntity.status(HttpStatus.OK).body(memberService.addMember(memberDTO))

    @Operation(method = "POST", summary = "사용자 로그인", description = "사용자 로그인 API")
    @PostMapping("/login")
    fun login(email: String?, password: String?) : ResponseEntity<TokenDTO>
        = ResponseEntity.status(HttpStatus.OK).body(memberService.login(email, password))

    @Operation(method = "POST", summary = "사용자 로그아웃", description = "사용자 로그아웃 API")
    @PostMapping("/logout")
    fun logout(@AuthenticationPrincipal user: User, @RequestHeader("Authorization") token: String): ResponseEntity<String>
        = ResponseEntity.status(HttpStatus.OK).body(memberService.logout(user.username, token))

    @Operation(method = "POST", summary = "토큰 재발급", description = "REFRESH 토큰 재발급 API")
    @PostMapping("/reissue")
    fun reissue(@RequestHeader("REFRESH_TOKEN", required = false) refreshToken: String?): ResponseEntity<Any> {
        refreshToken?.let {
            return ResponseEntity.ok(memberService.reissue(refreshToken))
        } ?: run {
            return ResponseEntity
                .status(CustomExceptionCode.BAD_REFRESH_TOKEN_INFO.status)
                .body(CustomExceptionCode.BAD_REFRESH_TOKEN_INFO.message)
        }
    }

    @Operation(method = "GET", summary = "로그인한 사용자 정보 조회 API")
    @GetMapping("/info")
    fun memberInfo(@AuthenticationPrincipal user: User): ResponseEntity<MemberDTO>
        = memberService.getMemberInfo(user.username)
            .let { return ResponseEntity.status(HttpStatus.OK).body(it) }

    @Operation(method = "PUT", summary = "사용자 정보 수정 API")
    @PutMapping("/update")
    fun updateMember(memberDTO: MemberDTO): ResponseEntity<String>
        = ResponseEntity.status(HttpStatus.OK).body(memberService.updateMember(memberDTO))

    @Operation(method = "DELETE", summary = "사용자 정보 삭제")
    @DeleteMapping("/delete")
    fun deleteMember(@AuthenticationPrincipal user: User): ResponseEntity<String>
        = ResponseEntity.status(HttpStatus.OK).body(memberService.deleteMember(user.username))

    @Operation(method = "GET", summary = "이메일로 사용자 정보 조회 API")
    @GetMapping("/info/email")
    fun getInfoByEmail(@RequestParam email: String): ResponseEntity<MemberDTO>
        = memberService.getMemberInfo(email)
            .let { return ResponseEntity.status(HttpStatus.OK).body(it) }
}