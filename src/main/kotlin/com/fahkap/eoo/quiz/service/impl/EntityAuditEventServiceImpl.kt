package com.fahkap.eoo.quiz.service.impl

import com.fahkap.eoo.quiz.repository.EntityAuditEventRepository
import com.fahkap.eoo.quiz.service.EntityAuditEventService
import com.fahkap.eoo.quiz.service.dto.EntityAuditEventDTO
import com.fahkap.eoo.quiz.service.mapper.EntityAuditEventMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class EntityAuditEventServiceImpl(
    private val entityAuditEventRepository: EntityAuditEventRepository,
    private val entityAuditEventMapper: EntityAuditEventMapper
) : EntityAuditEventService {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun findAllByEntityTypeAndEntityId(entityType: String, entityId: Long): List<EntityAuditEventDTO> {
        log.debug("Request to get all Audit entities by type and id")

        return entityAuditEventRepository.findAllByEntityTypeAndEntityId(entityType, entityId)
            .map(entityAuditEventMapper::toDto)
    }

    override fun findAllByEntityType(entityType: String, pageRequest: Pageable): Page<EntityAuditEventDTO> {
        log.debug("Request to get all Audit entities by type")

        return entityAuditEventRepository.findAllByEntityType(entityType, pageRequest)
            .map(entityAuditEventMapper::toDto)
    }

    override fun findAllEntityTypes(): MutableList<String> {
        log.debug("Request to get all Audit entities types")

        return entityAuditEventRepository.findAllEntityTypes()
    }

    override fun findOneByEntityTypeAndEntityIdAndCommitVersion(entityType: String, entityId: String, commitVersion: Int): EntityAuditEventDTO? {

        log.debug("Request to get Audit entity by type, id and commit version")

        return entityAuditEventRepository.findOneByEntityTypeAndEntityIdAndNextCommitVersion(entityType, entityId, commitVersion)?.let { entityAuditEventMapper.toDto(it) }
    }
}
