package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.service.QResultService
import com.fahkap.eoo.quiz.service.dto.QResultDTO
import com.fahkap.eoo.quiz.web.rest.errors.BadRequestAlertException
import io.github.jhipster.web.util.HeaderUtil
import io.github.jhipster.web.util.PaginationUtil
import io.github.jhipster.web.util.ResponseUtil
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.net.URISyntaxException
import javax.validation.Valid

private const val ENTITY_NAME = "eooQuizQResult"

/**
 * REST controller for managing [com.fahkap.eoo.quiz.domain.QResult].
 */
@RestController
@RequestMapping("/api")
class QResultResource(
    private val qResultService: QResultService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /q-results` : Create a new qResult.
     *
     * @param qResultDTO the qResultDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new qResultDTO, or with status `400 (Bad Request)` if the qResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/q-results")
    fun createQResult(@Valid @RequestBody qResultDTO: QResultDTO): ResponseEntity<QResultDTO> {
        log.debug("REST request to save QResult : {}", qResultDTO)
        if (qResultDTO.id != null) {
            throw BadRequestAlertException(
                "A new qResult cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = qResultService.save(qResultDTO)
        return ResponseEntity.created(URI("/api/q-results/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id))
            .body(result)
    }

    /**
     * `PUT  /q-results` : Updates an existing qResult.
     *
     * @param qResultDTO the qResultDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated qResultDTO,
     * or with status `400 (Bad Request)` if the qResultDTO is not valid,
     * or with status `500 (Internal Server Error)` if the qResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/q-results")
    fun updateQResult(@Valid @RequestBody qResultDTO: QResultDTO): ResponseEntity<QResultDTO> {
        log.debug("REST request to update QResult : {}", qResultDTO)
        if (qResultDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = qResultService.save(qResultDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    qResultDTO.id
                )
            )
            .body(result)
    }

    /**
     * `GET  /q-results` : get all the qResults.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of qResults in body.
     */
    @GetMapping("/q-results")
    fun getAllQResults(pageable: Pageable): ResponseEntity<List<QResultDTO>> {
        log.debug("REST request to get a page of QResults")
        val page = qResultService.findAll(pageable)
        val headers =
            PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /q-results/:id` : get the "id" qResult.
     *
     * @param id the id of the qResultDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the qResultDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/q-results/{id}")
    fun getQResult(@PathVariable id: String): ResponseEntity<QResultDTO> {
        log.debug("REST request to get QResult : {}", id)
        val qResultDTO = qResultService.findOne(id)
        return ResponseUtil.wrapOrNotFound(qResultDTO)
    }

    /**
     *  `DELETE  /q-results/:id` : delete the "id" qResult.
     *
     * @param id the id of the qResultDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/q-results/{id}")
    fun deleteQResult(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete QResult : {}", id)

        qResultService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
    }
}
