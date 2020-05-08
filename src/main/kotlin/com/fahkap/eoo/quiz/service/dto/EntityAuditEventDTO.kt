package com.fahkap.eoo.quiz.service.dto

import java.io.Serializable
import java.time.Instant
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A DTO for the [com.fahkap.eoo.quiz.domain.EntityAuditEvent] entity.
 */
data class EntityAuditEventDTO(

    var id: String? = null,

    @get: NotNull
    var entityId: String? = null,

    @get: NotNull
    @get: Size(min = 2, max = 255)
    var entityType: String? = null,

    @get: NotNull
    @get: Size(min = 2, max = 20)
    var action: String? = null,

    var entityValue: String? = null,

    var commitVersion: Int? = null,

    @get: Size(max = 100)
    var modifiedBy: String? = null,

    @get: NotNull
    var modifiedDate: Instant? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EntityAuditEventDTO

        if (id != other.id) return false
        if (entityId != other.entityId) return false
        if (entityType != other.entityType) return false
        if (action != other.action) return false
        if (entityValue != other.entityValue) return false
        if (commitVersion != other.commitVersion) return false
        if (modifiedBy != other.modifiedBy) return false
        if (modifiedDate != other.modifiedDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (entityId?.hashCode() ?: 0)
        result = 31 * result + (entityType?.hashCode() ?: 0)
        result = 31 * result + (action?.hashCode() ?: 0)
        result = 31 * result + (entityValue?.hashCode() ?: 0)
        result = 31 * result + (commitVersion?.hashCode() ?: 0)
        result = 31 * result + (modifiedBy?.hashCode() ?: 0)
        result = 31 * result + (modifiedDate?.hashCode() ?: 0)
        return result
    }
}
