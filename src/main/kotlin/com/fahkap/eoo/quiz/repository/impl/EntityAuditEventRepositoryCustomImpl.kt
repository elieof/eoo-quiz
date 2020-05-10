package com.fahkap.eoo.quiz.repository.impl

import com.fahkap.eoo.quiz.domain.EntityAuditEvent
import com.fahkap.eoo.quiz.repository.EntityAuditEventRepositoryCustom
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.lt
import org.springframework.stereotype.Component

@Component
class EntityAuditEventRepositoryCustomImpl(
    private val mongoTemplate: MongoTemplate
) : EntityAuditEventRepositoryCustom {

    override fun findLastEntityAuditedEvent(type: String, entityId: String): EntityAuditEvent? {
        val query = Query(
            Criteria().andOperator(
                EntityAuditEvent::entityType isEqualTo type,
                EntityAuditEvent::entityId isEqualTo entityId
            )
        )
            .limit(1)
            .with(Sort.by(Sort.Direction.DESC, "commit_version"))

        return mongoTemplate.findOne<EntityAuditEvent>(query)
    }

    override fun findAllEntityTypes(): MutableList<String> {
        return mongoTemplate.query(EntityAuditEvent::class.java)
            .distinct("entity_type")
            .`as`(String::class.java)
            .all()
    }

    override fun findOneByEntityTypeAndEntityIdAndNextCommitVersion(
        type: String,
        entityId: String,
        commitVersion: Int
    ): EntityAuditEvent? {
        val entityAuditEvent = findMaxCommitVersion(type, entityId, commitVersion)

        entityAuditEvent?.let {
            val query = Query(
                Criteria().andOperator(
                    EntityAuditEvent::entityType isEqualTo type,
                    EntityAuditEvent::entityId isEqualTo entityId,
                    EntityAuditEvent::commitVersion isEqualTo entityAuditEvent.commitVersion
                )
            )

            return mongoTemplate.findOne<EntityAuditEvent>(query)
        }
        return null
    }

    private fun findMaxCommitVersion(type: String, entityId: String, commitVersion: Int): EntityAuditEvent? {
        val query = Query(
            Criteria().andOperator(
                EntityAuditEvent::entityType isEqualTo type,
                EntityAuditEvent::entityId isEqualTo entityId,
                EntityAuditEvent::commitVersion lt commitVersion
            )
        )
            .limit(1)
            .with(Sort.by(Sort.Direction.DESC, "commit_version"))

        return mongoTemplate.findOne<EntityAuditEvent>(query)
    }
}
