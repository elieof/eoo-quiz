package com.fahkap.eoo.quiz.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A Proposition.
 */
@Document(collection = "proposition")
data class Proposition(
    @Id
    var id: String? = null,
    @get: NotNull
    @get: Size(min = 2)
    @Field("statement")
    var statement: String? = null,

    @get: NotNull
    @Field("valid")
    var valid: Boolean? = null,

    @Field("explanation")
    var explanation: String? = null,

    @DBRef
    @Field("question")
    @JsonIgnoreProperties(value = ["propositions"], allowSetters = true)
    var question: Question? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Proposition) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Proposition{" +
        "id=$id" +
        ", statement='$statement'" +
        ", valid='$valid'" +
        ", explanation='$explanation'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
