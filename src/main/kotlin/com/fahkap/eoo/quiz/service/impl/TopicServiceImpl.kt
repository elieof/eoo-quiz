package com.fahkap.eoo.quiz.service.impl

import com.fahkap.eoo.quiz.domain.Topic
import com.fahkap.eoo.quiz.repository.TopicRepository
import com.fahkap.eoo.quiz.service.TopicService
import com.fahkap.eoo.quiz.service.dto.TopicDTO
import com.fahkap.eoo.quiz.service.mapper.TopicMapper
import java.util.Optional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Service Implementation for managing [Topic].
 */
@Service
class TopicServiceImpl(
    private val topicRepository: TopicRepository,
    private val topicMapper: TopicMapper
) : TopicService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(topicDTO: TopicDTO): TopicDTO {
        log.debug("Request to save Topic : {}", topicDTO)

        var topic = topicMapper.toEntity(topicDTO)
        topic = topicRepository.save(topic)
        return topicMapper.toDto(topic)
    }

    /**
     * Get all the topics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    override fun findAll(pageable: Pageable): Page<TopicDTO> {
        log.debug("Request to get all Topics")
        return topicRepository.findAll(pageable)
            .map(topicMapper::toDto)
    }

    /**
     * Get one topic by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    override fun findOne(id: String): Optional<TopicDTO> {
        log.debug("Request to get Topic : {}", id)
        return topicRepository.findById(id)
            .map(topicMapper::toDto)
    }

    /**
     * Delete the topic by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: String) {
        log.debug("Request to delete Topic : {}", id)

        topicRepository.deleteById(id)
    }
}
