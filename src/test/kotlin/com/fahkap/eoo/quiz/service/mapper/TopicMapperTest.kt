package com.fahkap.eoo.quiz.service.mapper

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
}
