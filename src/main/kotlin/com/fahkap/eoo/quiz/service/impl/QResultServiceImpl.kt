package com.fahkap.eoo.quiz.service.impl

import com.fahkap.eoo.quiz.domain.QResult
import com.fahkap.eoo.quiz.repository.QResultRepository
import com.fahkap.eoo.quiz.service.QResultService
import com.fahkap.eoo.quiz.service.dto.QResultDTO
import com.fahkap.eoo.quiz.service.mapper.QResultMapper
import java.util.Optional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Service Implementation for managing [QResult].
 */
@Service
class QResultServiceImpl(
    private val qResultRepository: QResultRepository,
    private val qResultMapper: QResultMapper
) : QResultService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a qResult.
     *
     * @param qResultDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(qResultDTO: QResultDTO): QResultDTO {
        log.debug("Request to save QResult : {}", qResultDTO)

        var qResult = qResultMapper.toEntity(qResultDTO)
        qResult = qResultRepository.save(qResult)
        return qResultMapper.toDto(qResult)
    }

    /**
     * Get all the qResults.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    override fun findAll(pageable: Pageable): Page<QResultDTO> {
        log.debug("Request to get all QResults")
        return qResultRepository.findAll(pageable)
            .map(qResultMapper::toDto)
    }

    /**
     * Get one qResult by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    override fun findOne(id: String): Optional<QResultDTO> {
        log.debug("Request to get QResult : {}", id)
        return qResultRepository.findById(id)
            .map(qResultMapper::toDto)
    }

    /**
     * Delete the qResult by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: String) {
        log.debug("Request to delete QResult : {}", id)

        qResultRepository.deleteById(id)
    }
}
