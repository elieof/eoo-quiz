package com.fahkap.eoo.quiz.service.dto

import com.fahkap.eoo.quiz.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class QuestionDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(QuestionDTO::class)
        val questionDTO1 = QuestionDTO()
        questionDTO1.id = "id1"
        val questionDTO2 = QuestionDTO()
        assertThat(questionDTO1).isNotEqualTo(questionDTO2)
        questionDTO2.id = questionDTO1.id
        assertThat(questionDTO1).isEqualTo(questionDTO2)
        questionDTO2.id = "id2"
        assertThat(questionDTO1).isNotEqualTo(questionDTO2)
        questionDTO1.id = null
        assertThat(questionDTO1).isNotEqualTo(questionDTO2)
    }
}
