package com.fahkap.eoo.quiz.repository

import com.fahkap.eoo.quiz.domain.EntityAuditEvent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [EntityAuditEvent] entity.
 */
@Suppress("unused")
@Repository
interface EntityAuditEventRepository : MongoRepository<EntityAuditEvent, String>, EntityAuditEventRepositoryCustom {

    @Query("{and :[{entity_type : ?0}, {entity_id : $1}]}", sort = "{commit_version : -1}")
    fun findMaxCommitVersion(
        @Param("type") type: String?,
        @Param("entityId") entityId: Long?
    ): Int?

    fun findAllByEntityTypeAndEntityId(entityType: String, entityId: String): MutableList<EntityAuditEvent>

    fun findAllByEntityType(entityType: String, pageRequest: Pageable): Page<EntityAuditEvent>
}
