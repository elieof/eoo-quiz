package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.service.PropositionService
import com.fahkap.eoo.quiz.service.dto.PropositionDTO
import com.fahkap.eoo.quiz.web.rest.errors.BadRequestAlertException
import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import java.net.URI
import java.net.URISyntaxException
import javax.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

private const val ENTITY_NAME = "eooQuizProposition"
/**
 * REST controller for managing [com.fahkap.eoo.quiz.domain.Proposition].
 */
@RestController
@RequestMapping("/api")
class PropositionResource(
    private val propositionService: PropositionService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /propositions` : Create a new proposition.
     *
     * @param propositionDTO the propositionDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new propositionDTO, or with status `400 (Bad Request)` if the proposition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/propositions")
    fun createProposition(@Valid @RequestBody propositionDTO: PropositionDTO): ResponseEntity<PropositionDTO> {
        log.debug("REST request to save Proposition : {}", propositionDTO)
        if (propositionDTO.id != null) {
            throw BadRequestAlertException(
                "A new proposition cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = propositionService.save(propositionDTO)
        return ResponseEntity.created(URI("/api/propositions/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id))
            .body(result)
    }

    /**
     * `PUT  /propositions` : Updates an existing proposition.
     *
     * @param propositionDTO the propositionDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated propositionDTO,
     * or with status `400 (Bad Request)` if the propositionDTO is not valid,
     * or with status `500 (Internal Server Error)` if the propositionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/propositions")
    fun updateProposition(@Valid @RequestBody propositionDTO: PropositionDTO): ResponseEntity<PropositionDTO> {
        log.debug("REST request to update Proposition : {}", propositionDTO)
        if (propositionDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = propositionService.save(propositionDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     propositionDTO.id
                )
            )
            .body(result)
    }
    /**
     * `GET  /propositions` : get all the propositions.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of propositions in body.
     */
    @GetMapping("/propositions")
    fun getAllPropositions(pageable: Pageable): ResponseEntity<List<PropositionDTO>> {
        log.debug("REST request to get a page of Propositions")
        val page = propositionService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /propositions/:id` : get the "id" proposition.
     *
     * @param id the id of the propositionDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the propositionDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/propositions/{id}")
    fun getProposition(@PathVariable id: String): ResponseEntity<PropositionDTO> {
        log.debug("REST request to get Proposition : {}", id)
        val propositionDTO = propositionService.findOne(id)
        return ResponseUtil.wrapOrNotFound(propositionDTO)
    }
    /**
     *  `DELETE  /propositions/:id` : delete the "id" proposition.
     *
     * @param id the id of the propositionDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/propositions/{id}")
    fun deleteProposition(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Proposition : {}", id)

        propositionService.delete(id)
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
    }
}
