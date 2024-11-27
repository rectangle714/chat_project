package com.chat_project.web.friends.entity

import com.chat_project.common.AuditableEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Friends(
    memberId: String
) {
    @Id @GeneratedValue
    @Column(name = "friends_id")
    var id:Long? = null

    var memberId:Long? = null

    var addedAt:LocalDateTime = LocalDateTime.now()

}