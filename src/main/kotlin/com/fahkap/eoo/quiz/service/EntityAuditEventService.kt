package com.fahkap.eoo.quiz.service

import com.fahkap.eoo.quiz.service.dto.EntityAuditEventDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface EntityAuditEventService {

    fun findAllByEntityTypeAndEntityId(entityType: String, entityId: String): List<EntityAuditEventDTO>

    fun findAllByEntityType(entityType: String, pageRequest: Pageable): Page<EntityAuditEventDTO>

    fun findAllEntityTypes(): MutableList<String>

    fun findOneByEntityTypeAndEntityIdAndNextCommitVersion(
        entityType: String,
        entityId: String,
        commitVersion: Int
    ): EntityAuditEventDTO?
}
