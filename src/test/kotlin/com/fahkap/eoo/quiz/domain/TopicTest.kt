package com.fahkap.eoo.quiz.domain

import com.fahkap.eoo.quiz.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TopicTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Topic::class)
        val topic1 = Topic()
        topic1.id = "id1"
        val topic2 = Topic()
        topic2.id = topic1.id
        assertThat(topic1).isEqualTo(topic2)
        topic2.id = "id2"
        assertThat(topic1).isNotEqualTo(topic2)
        topic1.id = null
        assertThat(topic1).isNotEqualTo(topic2)
    }
}
