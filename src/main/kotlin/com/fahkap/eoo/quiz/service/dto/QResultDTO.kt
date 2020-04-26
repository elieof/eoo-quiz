package com.fahkap.eoo.quiz.service.dto

import java.io.Serializable
import javax.validation.constraints.NotNull

/**
 * A DTO for the [com.fahkap.eoo.quiz.domain.QResult] entity.
 */
data class QResultDTO(

    var id: String? = null,

    var username: String? = null,

    @get: NotNull
    var valid: Boolean? = null,

    var questionId: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QResultDTO) return false
        val qResultDTO = other
        if (qResultDTO.id == null || id == null) {
            return false
        }
        return id == qResultDTO.id
    }

    override fun hashCode() = id.hashCode()
}
