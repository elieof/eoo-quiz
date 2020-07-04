package com.fahkap.eoo.quiz.service.dto

import java.io.Serializable
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A DTO for the [com.fahkap.eoo.quiz.domain.Question] entity.
 */
data class QuestionDTO(

    var id: String? = null,

    @get: NotNull
    @get: Size(min = 3)
    var statement: String? = null,

    @get: NotNull
    var level: Int? = null,

    var topicId: String? = null,

    var quizId: String? = null

) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QuestionDTO) return false
        return id != null && id == other.id
    }

    override fun hashCode() = 31
}
