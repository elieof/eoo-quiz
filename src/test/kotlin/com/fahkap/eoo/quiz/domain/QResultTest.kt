package com.fahkap.eoo.quiz.domain

import com.fahkap.eoo.quiz.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class QResultTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(QResult::class)
        val qResult1 = QResult()
        qResult1.id = "id1"
        val qResult2 = QResult()
        qResult2.id = qResult1.id
        assertThat(qResult1).isEqualTo(qResult2)
        qResult2.id = "id2"
        assertThat(qResult1).isNotEqualTo(qResult2)
        qResult1.id = null
        assertThat(qResult1).isNotEqualTo(qResult2)
    }
}
