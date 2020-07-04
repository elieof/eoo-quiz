package com.fahkap.eoo.quiz.domain

import com.fahkap.eoo.quiz.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class QuizTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Quiz::class)
        val quiz1 = Quiz()
        quiz1.id = "id1"
        val quiz2 = Quiz()
        quiz2.id = quiz1.id
        assertThat(quiz1).isEqualTo(quiz2)
        quiz2.id = "id2"
        assertThat(quiz1).isNotEqualTo(quiz2)
        quiz1.id = null
        assertThat(quiz1).isNotEqualTo(quiz2)
    }
}
