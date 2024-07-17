package com.chat_project.web.security

import com.chat_project.common.constant.Role
import com.chat_project.web.member.entity.Member
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails private constructor(
    private val email: String,
    private val password: String,
    private val nickname: String,
    private val role: Role
): UserDetails  {
    companion object {
        fun from(member: Member) : CustomUserDetails {
            return with(member) {
                CustomUserDetails(
                    email = member.email,
                    password =  member.password,
                    nickname = member.nickname,
                    role = member.role
                )
            }
        }
    }

    override fun getUsername(): String {
        return email
    }

    override fun getPassword(): String {
        return password
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
//        return role.stream().map{ role -> SimpleGrantedAuthority("ROLE_$role") }.collect(Collectors.toSet())
        return arrayListOf(SimpleGrantedAuthority(role.value))
    }

    override fun isAccountNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isAccountNonLocked(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isCredentialsNonExpired(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isEnabled(): Boolean {
        TODO("Not yet implemented")
    }
}
