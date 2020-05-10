package com.fahkap.eoo.quiz.service.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A DTO for the [com.fahkap.eoo.quiz.domain.Topic] entity.
 */
data class TopicDTO(

    var id: String? = null,

    @get: NotNull
    @get: Size(min = 2, max = 50)
    var name: String? = null

) : AbstractAuditingDTO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TopicDTO) return false
        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = id.hashCode()
}
