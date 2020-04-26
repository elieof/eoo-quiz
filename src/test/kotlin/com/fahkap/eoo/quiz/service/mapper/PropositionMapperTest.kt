package com.fahkap.eoo.quiz.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class PropositionMapperTest {

    private lateinit var propositionMapper: PropositionMapper

    @BeforeEach
    fun setUp() {
        propositionMapper = PropositionMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = "id1"
        assertThat(propositionMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(propositionMapper.fromId(null)).isNull()
    }
}
