package com.fahkap.eoo.quiz.repository

import com.fahkap.eoo.quiz.domain.EntityAuditEvent

interface EntityAuditEventRepositoryCustom {

    fun findMaxCommitVersion(type: String, entityId: String): EntityAuditEvent?

    fun findAllEntityTypes(): MutableList<String>

    fun findOneByEntityTypeAndEntityIdAndCommitVersion(type: String, entityId: String, commitVersion: Int): EntityAuditEvent?
}
