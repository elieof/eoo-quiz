package com.fahkap.eoo.quiz.service.dto

import java.io.Serializable
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A DTO for the [com.fahkap.eoo.quiz.domain.Proposition] entity.
 */
data class PropositionDTO(

    var id: String? = null,

    @get: NotNull
    @get: Size(min = 2)
    var statement: String? = null,

    @get: NotNull
    var valid: Boolean? = null,

    var explanation: String? = null,

    var questionId: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PropositionDTO) return false
        val propositionDTO = other as PropositionDTO
        if (propositionDTO.id == null || id == null) {
            return false
        }
        return id == propositionDTO.id
    }

    override fun hashCode() = id.hashCode()
}
