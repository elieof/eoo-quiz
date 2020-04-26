package com.fahkap.eoo.quiz.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class QResultMapperTest {

    private lateinit var qResultMapper: QResultMapper

    @BeforeEach
    fun setUp() {
        qResultMapper = QResultMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = "id1"
        assertThat(qResultMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(qResultMapper.fromId(null)).isNull()
    }
}
