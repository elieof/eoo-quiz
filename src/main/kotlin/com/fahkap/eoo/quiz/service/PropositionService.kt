package com.fahkap.eoo.quiz.service
import com.fahkap.eoo.quiz.service.dto.PropositionDTO
import java.util.Optional
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

/**
 * Service Interface for managing [com.fahkap.eoo.quiz.domain.Proposition].
 */
interface PropositionService {

    /**
     * Save a proposition.
     *
     * @param propositionDTO the entity to save.
     * @return the persisted entity.
     */
    fun save(propositionDTO: PropositionDTO): PropositionDTO

    /**
     * Get all the propositions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    fun findAll(pageable: Pageable): Page<PropositionDTO>

    /**
     * Get the "id" proposition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    fun findOne(id: String): Optional<PropositionDTO>

    /**
     * Delete the "id" proposition.
     *
     * @param id the id of the entity.
     */
    fun delete(id: String)
}
