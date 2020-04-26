package com.fahkap.eoo.quiz.service.impl

import com.fahkap.eoo.quiz.domain.Quiz
import com.fahkap.eoo.quiz.repository.QuizRepository
import com.fahkap.eoo.quiz.service.QuizService
import com.fahkap.eoo.quiz.service.dto.QuizDTO
import com.fahkap.eoo.quiz.service.mapper.QuizMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

/**
 * Service Implementation for managing [Quiz].
 */
@Service
class QuizServiceImpl(
    private val quizRepository: QuizRepository,
    private val quizMapper: QuizMapper
) : QuizService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a quiz.
     *
     * @param quizDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(quizDTO: QuizDTO): QuizDTO {
        log.debug("Request to save Quiz : {}", quizDTO)

        var quiz = quizMapper.toEntity(quizDTO)
        quiz = quizRepository.save(quiz)
        return quizMapper.toDto(quiz)
    }

    /**
     * Get all the quizzes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    override fun findAll(pageable: Pageable): Page<QuizDTO> {
        log.debug("Request to get all Quizzes")
        return quizRepository.findAll(pageable)
            .map(quizMapper::toDto)
    }

    /**
     * Get one quiz by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    override fun findOne(id: String): Optional<QuizDTO> {
        log.debug("Request to get Quiz : {}", id)
        return quizRepository.findById(id)
            .map(quizMapper::toDto)
    }

    /**
     * Delete the quiz by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: String) {
        log.debug("Request to delete Quiz : {}", id)

        quizRepository.deleteById(id)
    }
}
