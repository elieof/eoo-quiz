package com.fahkap.eoo.quiz.service

import com.fahkap.eoo.quiz.service.dto.TopicDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [com.fahkap.eoo.quiz.domain.Topic].
 */
interface TopicService {

    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(topicDTO: TopicDTO): TopicDTO

    /**
     * Get all the topics.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<TopicDTO>

    /**
     * Get the "id" topic.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<TopicDTO>

    /**
     * Delete the "id" topic.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String)
}
