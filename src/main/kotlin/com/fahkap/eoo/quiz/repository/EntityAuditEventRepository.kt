package com.fahkap.eoo.quiz.repository

import com.fahkap.eoo.quiz.domain.EntityAuditEvent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [EntityAuditEvent] entity.
 */
@Suppress("unused")
@Repository
interface EntityAuditEventRepository : MongoRepository<EntityAuditEvent, String>, EntityAuditEventRepositoryCustom {

    fun findAllByEntityTypeAndEntityId(entityType: String, entityId: Long): MutableList<EntityAuditEvent>

    fun findAllByEntityType(entityType: String, pageRequest: Pageable): Page<EntityAuditEvent>
}
