package com.fahkap.eoo.quiz.service.dto

import com.fahkap.eoo.quiz.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class QResultDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(QResultDTO::class)
        val qResultDTO1 = QResultDTO()
        qResultDTO1.id = "id1"
        val qResultDTO2 = QResultDTO()
        assertThat(qResultDTO1).isNotEqualTo(qResultDTO2)
        qResultDTO2.id = qResultDTO1.id
        assertThat(qResultDTO1).isEqualTo(qResultDTO2)
        qResultDTO2.id = "id2"
        assertThat(qResultDTO1).isNotEqualTo(qResultDTO2)
        qResultDTO1.id = null
        assertThat(qResultDTO1).isNotEqualTo(qResultDTO2)
    }
}
