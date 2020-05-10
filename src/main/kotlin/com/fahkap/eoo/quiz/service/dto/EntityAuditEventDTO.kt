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
        if (other !is EntityAuditEventDTO) return false
        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = id.hashCode()
}
