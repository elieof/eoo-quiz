package com.fahkap.eoo.quiz.service.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A DTO for the [com.fahkap.eoo.quiz.domain.Quiz] entity.
 */
data class QuizDTO(

    var id: String? = null,

    @get: NotNull
    @get: Size(min = 2)
    var name: String? = null,

    var description: String? = null

) : AbstractAuditingDTO() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuizDTO) return false
        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = id.hashCode()
}
