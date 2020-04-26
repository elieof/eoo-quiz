package com.fahkap.eoo.quiz.service.mapper

import com.fahkap.eoo.quiz.domain.Quiz
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

    @Test
    fun testAuditValues() {
        var entity = Quiz("id1", "name1")
        entity.createdBy = "owner"
        entity.lastModifiedBy = "owner"
        val dto = quizMapper.toDto(entity)
        assertThat(dto).isNotNull
        assertThat(dto.id).isEqualTo(entity.id)
        assertThat(dto.name).isEqualTo(entity.name)
        assertThat(dto.description).isEqualTo(entity.description)
        assertThat(dto.createdBy).isEqualTo(entity.createdBy)
        assertThat(dto.lastModifiedBy).isEqualTo(entity.lastModifiedBy)
        assertThat(dto.createdDate).isEqualTo(entity.createdDate).isNotNull
        assertThat(dto.lastModifiedDate).isEqualTo(entity.lastModifiedDate).isNotNull
    }
}
