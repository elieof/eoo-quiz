package com.fahkap.eoo.quiz.service.dto

import com.fahkap.eoo.quiz.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PropositionDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(PropositionDTO::class)
        val propositionDTO1 = PropositionDTO()
        propositionDTO1.id = "id1"
        val propositionDTO2 = PropositionDTO()
        assertThat(propositionDTO1).isNotEqualTo(propositionDTO2)
        propositionDTO2.id = propositionDTO1.id
        assertThat(propositionDTO1).isEqualTo(propositionDTO2)
        propositionDTO2.id = "id2"
        assertThat(propositionDTO1).isNotEqualTo(propositionDTO2)
        propositionDTO1.id = null
        assertThat(propositionDTO1).isNotEqualTo(propositionDTO2)
    }
}
