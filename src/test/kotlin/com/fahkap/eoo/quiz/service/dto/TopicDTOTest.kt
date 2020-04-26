package com.fahkap.eoo.quiz.service.dto

import com.fahkap.eoo.quiz.web.rest.equalsVerifier
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TopicDTOTest {

    @Test
    fun dtoEqualsVerifier() {
        equalsVerifier(TopicDTO::class)
        val topicDTO1 = TopicDTO()
        topicDTO1.id = "id1"
        val topicDTO2 = TopicDTO()
        assertThat(topicDTO1).isNotEqualTo(topicDTO2)
        topicDTO2.id = topicDTO1.id
        assertThat(topicDTO1).isEqualTo(topicDTO2)
        topicDTO2.id = "id2"
        assertThat(topicDTO1).isNotEqualTo(topicDTO2)
        topicDTO1.id = null
        assertThat(topicDTO1).isNotEqualTo(topicDTO2)
    }
}
