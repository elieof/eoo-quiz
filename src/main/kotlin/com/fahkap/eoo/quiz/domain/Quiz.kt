package com.fahkap.eoo.quiz.domain

import java.io.Serializable
import javax.validation.constraints.*
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

/**
 * A Quiz.
 */
@Document(collection = "quiz")
data class Quiz(
    @Id
    var id: String? = null,
    @get: NotNull
    @get: Size(min = 2)
    @Field("name")
    var name: String? = null,

    @Field("description")
    var description: String? = null,

    @DBRef
    @Field("question")
    var questions: MutableSet<Question> = mutableSetOf()

    // jhipster-needle-entity-add-field - JHipster will add fields here
) : Serializable {

    fun addQuestion(question: Question): Quiz {
        this.questions.add(question)
        question.quiz = this
        return this
    }

    fun removeQuestion(question: Question): Quiz {
        this.questions.remove(question)
        question.quiz = null
        return this
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Quiz) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Quiz{" +
        "id=$id" +
        ", name='$name'" +
        ", description='$description'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
