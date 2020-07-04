package com.fahkap.eoo.quiz.service.mapper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class QuizMapperTest {

    private lateinit var quizMapper: QuizMapper

    @BeforeEach
    fun setUp() {
        quizMapper = QuizMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = "id1"
        assertThat(quizMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(quizMapper.fromId(null)).isNull()
    }
}
