package com.fahkap.eoo.quiz.service.mapper

import com.fahkap.eoo.quiz.domain.Topic
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TopicMapperTest {

    private lateinit var topicMapper: TopicMapper

    @BeforeEach
    fun setUp() {
        topicMapper = TopicMapperImpl()
    }

    @Test
    fun testEntityFromId() {
        val id = "id1"
        assertThat(topicMapper.fromId(id)?.id).isEqualTo(id)
        assertThat(topicMapper.fromId(null)).isNull()
    }

    @Test
    fun testAuditValues() {
        var entity = Topic("id1", "name1")
        entity.createdBy = "owner"
        entity.lastModifiedBy = "owner"
        val dto = topicMapper.toDto(entity)
        assertThat(dto).isNotNull
        assertThat(dto.id).isEqualTo(entity.id)
        assertThat(dto.name).isEqualTo(entity.name)
        assertThat(dto.createdBy).isEqualTo(entity.createdBy)
        assertThat(dto.lastModifiedBy).isEqualTo(entity.lastModifiedBy)
        assertThat(dto.createdDate).isEqualTo(entity.createdDate).isNotNull
        assertThat(dto.lastModifiedDate).isEqualTo(entity.lastModifiedDate).isNotNull
    }
}
