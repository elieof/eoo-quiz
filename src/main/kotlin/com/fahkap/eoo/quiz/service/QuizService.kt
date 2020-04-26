package com.fahkap.eoo.quiz.service

import com.fahkap.eoo.quiz.service.dto.QuizDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [com.fahkap.eoo.quiz.domain.Quiz].
 */
interface QuizService {

    /**
     * Save a quiz.
     *
     * @param quizDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(quizDTO: QuizDTO): QuizDTO

    /**
     * Get all the quizzes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<QuizDTO>

    /**
     * Get the "id" quiz.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<QuizDTO>

    /**
     * Delete the "id" quiz.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String)
}
