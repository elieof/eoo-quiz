package com.fahkap.eoo.quiz.service.dto

import com.fahkap.eoo.quiz.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class QuizDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(QuizDTO::class)
        val quizDTO1 = QuizDTO()
        quizDTO1.id = "id1"
        val quizDTO2 = QuizDTO()
        assertThat(quizDTO1).isNotEqualTo(quizDTO2)
        quizDTO2.id = quizDTO1.id
        assertThat(quizDTO1).isEqualTo(quizDTO2)
        quizDTO2.id = "id2"
        assertThat(quizDTO1).isNotEqualTo(quizDTO2)
        quizDTO1.id = null
        assertThat(quizDTO1).isNotEqualTo(quizDTO2)
    }
}
