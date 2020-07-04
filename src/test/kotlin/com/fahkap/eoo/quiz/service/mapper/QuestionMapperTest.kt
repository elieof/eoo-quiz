package com.fahkap.eoo.quiz.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class QuestionMapperTest {

    private lateinit var questionMapper: QuestionMapper

    @BeforeEach
    fun setUp() {
        questionMapper = QuestionMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = "id1"
        assertThat(questionMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(questionMapper.fromId(null)).isNull()
    }
}
