package com.fahkap.eoo.quiz.service.mapper

import com.fahkap.eoo.quiz.domain.EntityAuditEvent
import com.fahkap.eoo.quiz.service.dto.EntityAuditEventDTO
import org.mapstruct.Mapper

/**
 * Mapper for the entity [EntityAuditEvent] and its DTO [EntityAuditEventDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface EntityAuditEventMapper :
    EntityMapper<EntityAuditEventDTO, EntityAuditEvent> {

    @JvmDefault
    fun fromId(id: String?) = id?.let {
        val entityAuditEvent = EntityAuditEvent()
        entityAuditEvent.id = id
        entityAuditEvent
    }
}
