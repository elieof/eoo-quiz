package com.fahkap.eoo.quiz.config.audit

import com.fahkap.eoo.quiz.domain.AbstractAuditingEntity
import com.fahkap.eoo.quiz.domain.EntityAuditEvent
import com.fahkap.eoo.quiz.repository.EntityAuditEventRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class AsyncEntityAuditEventWriter(
    private val entityAuditEventRepository: EntityAuditEventRepository,
    private val objectMapper: ObjectMapper
) {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Writes audit events to DB asynchronously in a new thread
     */
    @Async
    fun writeAuditEvent(target: Any, action: EntityAuditAction = EntityAuditAction.DELETE) {
        log.debug("-------------- Post {} audit  --------------", action.value())
        try {
            val auditedEntity: EntityAuditEvent? = if (target is EntityAuditEvent) {
                target
            } else {
                prepareAuditEntity(target, action)
            }
            auditedEntity?.let {
                entityAuditEventRepository.save(it)
            }
        } catch (e: Exception) {
            log.error("Exception while persisting audit entity for {} error: {}", target, e)
        }
    }

    /**
     * Method to prepare auditing entity
     *
     * @param entity
     * @param action
     * @return
     */
    private fun prepareAuditEntity(entity: Any, action: EntityAuditAction): EntityAuditEvent? {
        val auditedEntity = EntityAuditEvent()
        val entityClass: Class<*> = entity.javaClass // Retrieve entity class with reflection
        auditedEntity.action = action.value()
        auditedEntity.entityType = entityClass.name
        val entityId: String
        val entityData: String

        log.trace("Getting Entity Id and Content")

        try {
            val privateStringField = entityClass.getDeclaredField("id")
            privateStringField.isAccessible = true
            entityId = privateStringField[entity] as String
            privateStringField.isAccessible = false
            entityData = objectMapper.writeValueAsString(entity)
        } catch (e: Exception) {
            log.error("Exception while getting entity ID and content {}", e)
            return null
        }
        auditedEntity.entityId = entityId
        auditedEntity.entityValue = entityData
        val abstractAuditEntity: AbstractAuditingEntity = entity as AbstractAuditingEntity
        if (EntityAuditAction.CREATE == action) {
            auditedEntity.modifiedBy = abstractAuditEntity.createdBy
            auditedEntity.modifiedDate = abstractAuditEntity.createdDate
            auditedEntity.commitVersion = 1
        } else {
            auditedEntity.modifiedBy = abstractAuditEntity.lastModifiedBy
            auditedEntity.modifiedDate = abstractAuditEntity.lastModifiedDate
            calculateVersion(auditedEntity)
        }
        log.trace("Audit Entity --> {} ", auditedEntity.toString())
        return auditedEntity
    }

    private fun calculateVersion(auditedEntity: EntityAuditEvent) {
        log.trace("Version calculation. for update/remove")
        val lastCommitVersion: Int? = getLastEntityAuditedEvent(
            auditedEntity.entityType!!, auditedEntity.entityId!!)?.commitVersion
        log.trace("Last commit version of entity => {}", lastCommitVersion)
        if (lastCommitVersion != null && lastCommitVersion != 0) {
            log.trace("Present. Adding version..")
            auditedEntity.commitVersion = lastCommitVersion + 1
        } else {
            log.trace("No entities.. Adding new version 1")
            auditedEntity.commitVersion = 1
        }
    }

    fun getLastEntityAuditedEvent(type: String, entityId: String): EntityAuditEvent? {
        return entityAuditEventRepository.findMaxCommitVersion(type, entityId)
    }
}
