package com.chat_project.common

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy

@MappedSuperclass
abstract class AuditableEntity(): BaseEntity() {
    @CreatedBy
    @Column(updatable = false)
    var registerId: String? = null

    @LastModifiedBy
    var updateId: String? = null
}