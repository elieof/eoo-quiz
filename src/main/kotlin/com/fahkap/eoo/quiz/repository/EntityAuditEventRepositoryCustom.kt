package com.fahkap.eoo.quiz.repository

import com.fahkap.eoo.quiz.domain.EntityAuditEvent

interface EntityAuditEventRepositoryCustom {

    fun findLastEntityAuditedEvent(type: String, entityId: String): EntityAuditEvent?

    fun findAllEntityTypes(): MutableList<String>

    fun findOneByEntityTypeAndEntityIdAndNextCommitVersion(
        type: String,
        entityId: String,
        commitVersion: Int
    ): EntityAuditEvent?
}
