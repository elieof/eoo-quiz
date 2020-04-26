package com.fahkap.eoo.quiz.service

import com.fahkap.eoo.quiz.service.dto.QResultDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

/**
 * Service Interface for managing [com.fahkap.eoo.quiz.domain.QResult].
 */
interface QResultService {

    /**
     * Save a qResult.
     *
     * @param qResultDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(qResultDTO: QResultDTO): QResultDTO

    /**
     * Get all the qResults.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<QResultDTO>

    /**
     * Get the "id" qResult.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<QResultDTO>

    /**
     * Delete the "id" qResult.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String)
}
