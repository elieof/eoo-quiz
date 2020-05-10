package com.fahkap.eoo.quiz.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable
import java.time.Instant
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Document(collection = "jhi_entity_audit_event")
data class EntityAuditEvent(
    @Id
    var id: String? = null,

    @field:NotNull
    @Field("entity_id")
    var entityId: String? = null,

    @field:NotNull
    @Field("entity_type")
    @get: Size(min = 2, max = 255)
    var entityType: String? = null,

    @field:NotNull
    @Field("action")
    @get: Size(min = 2, max = 20)
    var action: String? = null,

    @Field("entity_value")
    var entityValue: String? = null,

    @Field("commit_version")
    var commitVersion: Int? = null,

    @get: Size(max = 100)
    @Field("modified_by")
    var modifiedBy: String? = null,

    @field:NotNull
    @Field("modified_date")
    var modifiedDate: Instant? = null

) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EntityAuditEvent) return false
        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = id.hashCode()

}
