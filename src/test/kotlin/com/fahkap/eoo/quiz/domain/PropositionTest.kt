package com.fahkap.eoo.quiz.domain

import com.fahkap.eoo.quiz.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PropositionTest {

    @Test
    fun equalsVerifier() {
        equalsVerifier(Proposition::class)
        val proposition1 = Proposition()
        proposition1.id = "id1"
        val proposition2 = Proposition()
        proposition2.id = proposition1.id
        assertThat(proposition1).isEqualTo(proposition2)
        proposition2.id = "id2"
        assertThat(proposition1).isNotEqualTo(proposition2)
        proposition1.id = null
        assertThat(proposition1).isNotEqualTo(proposition2)
    }
}
