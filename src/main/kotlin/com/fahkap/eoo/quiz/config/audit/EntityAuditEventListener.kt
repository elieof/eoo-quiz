package com.fahkap.eoo.quiz.config.audit

import com.fahkap.eoo.quiz.domain.AbstractAuditingEntity
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent
import org.springframework.scheduling.annotation.Async
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Component
class EntityAuditEventListener(
    private val asyncEntityAuditEventWriter: AsyncEntityAuditEventWriter
) : AbstractMongoEventListener<AbstractAuditingEntity>() {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    override fun onAfterSave(event: AfterSaveEvent<AbstractAuditingEntity>) {
        super.onAfterSave(event)
        val auditedEntity = event.source
        if (auditedEntity.createdDate == null) {
            asyncEntityAuditEventWriter.writeAuditEvent(auditedEntity, EntityAuditAction.UPDATE)
        } else {
            asyncEntityAuditEventWriter.writeAuditEvent(auditedEntity, EntityAuditAction.CREATE)
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Async
    override fun onAfterDelete(event: AfterDeleteEvent<AbstractAuditingEntity>) {
        super.onAfterDelete(event)
        val authentication = SecurityContextHolder.getContext().authentication
        val auditedEntity = asyncEntityAuditEventWriter.getLastEntityAuditedEvent(
            event.type!!.typeName, event.source["_id"].toString()
        )
        auditedEntity?.entityValue = "{\"id\" : \"" + event.source["_id"].toString() + "\"}"
        auditedEntity?.action = EntityAuditAction.DELETE.value
        auditedEntity?.modifiedBy = authentication.name
        auditedEntity?.modifiedDate = Instant.now()

        auditedEntity?.let { asyncEntityAuditEventWriter.writeAuditEvent(it) }
    }
}
