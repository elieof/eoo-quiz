package com.fahkap.eoo.quiz.repository

import com.fahkap.eoo.quiz.domain.PersistentAuditEvent
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import java.time.Instant

interface PersistenceAuditEventRepository : MongoRepository<PersistentAuditEvent, String> {

    fun findByPrincipal(principal: String): List<PersistentAuditEvent>

    fun findByPrincipalAndAuditEventDateAfterAndAuditEventType(
        principal: String,
        after: Instant,
        type: String
    ): List<PersistentAuditEvent>

    fun findAllByAuditEventDateBetween(
        fromDate: Instant,
        toDate: Instant,
        pageable: Pageable
    ): Page<PersistentAuditEvent>

    fun findByAuditEventDateBefore(before: Instant): List<PersistentAuditEvent>
}
