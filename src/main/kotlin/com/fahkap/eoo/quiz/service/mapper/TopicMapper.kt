package com.fahkap.eoo.quiz.service.mapper

import com.fahkap.eoo.quiz.domain.Topic
import com.fahkap.eoo.quiz.service.dto.TopicDTO
import org.mapstruct.Mapper

/**
 * Mapper for the entity [Topic] and its DTO [TopicDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface TopicMapper :
    EntityMapper<TopicDTO, Topic> {

    override fun toEntity(topicDTO: TopicDTO): Topic

    @JvmDefault
    fun fromId(id: String?) = id?.let {
        val topic = Topic()
        topic.id = id
        topic
    }
}
