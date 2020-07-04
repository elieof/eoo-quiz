package com.fahkap.eoo.quiz.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable
import javax.validation.constraints.NotNull

/**
 * A QResult.
 */
@Document(collection = "q_result")
data class QResult(
    @Id
    var id: String? = null,

    @Field("username")
    var username: String? = null,

    @get: NotNull
    @Field("valid")
    var valid: Boolean? = null,

    @DBRef
    @Field("question")
    @JsonIgnoreProperties(value = ["qResults"], allowSetters = true)
    var question: Question? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is QResult) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "QResult{" +
        "id=$id" +
        ", username='$username'" +
        ", valid='$valid'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
