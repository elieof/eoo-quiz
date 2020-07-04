package com.fahkap.eoo.quiz.service.impl

import com.fahkap.eoo.quiz.domain.Proposition
import com.fahkap.eoo.quiz.repository.PropositionRepository
import com.fahkap.eoo.quiz.service.PropositionService
import com.fahkap.eoo.quiz.service.dto.PropositionDTO
import com.fahkap.eoo.quiz.service.mapper.PropositionMapper
import java.util.Optional
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

/**
 * Service Implementation for managing [Proposition].
 */
@Service
class PropositionServiceImpl(
    private val propositionRepository: PropositionRepository,
    private val propositionMapper: PropositionMapper
) : PropositionService {

    private val log = LoggerFactory.getLogger(javaClass)

    /**
     * Save a proposition.
     *
     * @param propositionDTO the entity to save.
     * @return the persisted entity.
     */
    override fun save(propositionDTO: PropositionDTO): PropositionDTO {
        log.debug("Request to save Proposition : {}", propositionDTO)

        var proposition = propositionMapper.toEntity(propositionDTO)
        proposition = propositionRepository.save(proposition)
        return propositionMapper.toDto(proposition)
    }

    /**
     * Get all the propositions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    override fun findAll(pageable: Pageable): Page<PropositionDTO> {
        log.debug("Request to get all Propositions")
        return propositionRepository.findAll(pageable)
            .map(propositionMapper::toDto)
    }

    /**
     * Get one proposition by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    override fun findOne(id: String): Optional<PropositionDTO> {
        log.debug("Request to get Proposition : {}", id)
        return propositionRepository.findById(id)
            .map(propositionMapper::toDto)
    }

    /**
     * Delete the proposition by id.
     *
     * @param id the id of the entity.
     */
    override fun delete(id: String) {
        log.debug("Request to delete Proposition : {}", id)

        propositionRepository.deleteById(id)
    }
}
