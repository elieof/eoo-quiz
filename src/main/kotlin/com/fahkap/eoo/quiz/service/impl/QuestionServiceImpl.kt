package com.fahkap.eoo.quiz.service.impl

import com.fahkap.eoo.quiz.domain.Question
import com.fahkap.eoo.quiz.repository.QuestionRepository
import com.fahkap.eoo.quiz.service.QuestionService
import com.fahkap.eoo.quiz.service.dto.QuestionDTO
import com.fahkap.eoo.quiz.service.mapper.QuestionMapper
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

/**
 * Service Implementation for managing [Question].
 */
@Service
class QuestionServiceImpl(
    private val questionRepository: QuestionRepository,
    private val questionMapper: QuestionMapper
) : QuestionService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(questionDTO: QuestionDTO): QuestionDTO {
        log.debug("Request to save Question : {}", questionDTO)

        var question = questionMapper.toEntity(questionDTO)
        question = questionRepository.save(question)
        return questionMapper.toDto(question)
    }

    /**
     * Get all the questions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    override fun findAll(pageable: Pageable): Page<QuestionDTO> {
        log.debug("Request to get all Questions")
        return questionRepository.findAll(pageable)
            .map(questionMapper::toDto)
    }

    /**
     * Get one question by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    override fun findOne(id: String): Optional<QuestionDTO> {
        log.debug("Request to get Question : {}", id)
        return questionRepository.findById(id)
            .map(questionMapper::toDto)
    }

    /**
     * Delete the question by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: String) {
        log.debug("Request to delete Question : {}", id)

        questionRepository.deleteById(id)
    }
}
