package com.fahkap.eoo.quiz.service

import com.fahkap.eoo.quiz.service.dto.QuestionDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [com.fahkap.eoo.quiz.domain.Question].
 */
interface QuestionService {

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(questionDTO: QuestionDTO): QuestionDTO

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<QuestionDTO>

    /**
     * Get the "id" question.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<QuestionDTO>

    /**
     * Delete the "id" question.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String)
}
