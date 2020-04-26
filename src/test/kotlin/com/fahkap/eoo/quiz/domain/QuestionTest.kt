package com.fahkap.eoo.quiz.domain

import com.fahkap.eoo.quiz.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class QuestionTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Question::class)
        val question1 = Question()
        question1.id = "id1"
        val question2 = Question()
        question2.id = question1.id
        assertThat(question1).isEqualTo(question2)
        question2.id = "id2"
        assertThat(question1).isNotEqualTo(question2)
        question1.id = null
        assertThat(question1).isNotEqualTo(question2)
    }
}
