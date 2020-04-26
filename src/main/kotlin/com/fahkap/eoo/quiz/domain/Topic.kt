package com.fahkap.eoo.quiz.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field
import java.io.Serializable
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * A Topic.
 */
@Document(collection = "topic")
data class Topic(
    @Id
    var id: String? = null,
    @get: NotNull
    @get: Size(min = 2, max = 50)
    @Field("name")
    var name: String? = null

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
) : AbstractAuditingEntity(), Serializable {
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Topic) return false

        return id != null && other.id != null && id == other.id
    }

    override fun hashCode() = 31

    override fun toString() = "Topic{" +
        "id=$id" +
        ", name='$name'" +
        "}"

    companion object {
        private const val serialVersionUID = 1L
    }
}
