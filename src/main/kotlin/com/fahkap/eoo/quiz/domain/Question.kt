package com.fahkap.eoo.quiz.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.io.Serializable
import javax.validation.constraints.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

/**
 * A Question.
 */
@Document(collection = "question")
data class Question(
    @Id
    var id: String? = null,
    @get: NotNull
    @get: Size(min = 3)
    @Field("statement")
    var statement: String? = null,

    @get: NotNull
    @Field("level")
    var level: Int? = null,

    @DBRef
    @Field("proposition")
    var propositions: MutableSet<Proposition> = mutableSetOf(),

    @DBRef
    @Field("topic")
    @JsonIgnoreProperties(value = ["questions"], allowSetters = true)
    var topic: Topic? = null,

    @DBRef
    @Field("quiz")
    @JsonIgnoreProperties(value = ["questions"], allowSetters = true)
    var quiz: Quiz? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    fun addProposition(proposition: Proposition): Question {
        this.propositions.add(proposition)
        proposition.question = this
        return this
    }

    fun removeProposition(proposition: Proposition): Question {
        this.propositions.remove(proposition)
        proposition.question = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Question) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Question{" +
        "id=$id" +
        ", statement='$statement'" +
        ", level=$level" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
