package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.service.TopicService
import com.fahkap.eoo.quiz.service.dto.TopicDTO
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

private const val ENTITY_NAME = "eooQuizTopic"

/**
 * REST controller for managing [com.fahkap.eoo.quiz.domain.Topic].
 */
@RestController
@RequestMapping("/api")
class TopicResource(
    private val topicService: TopicService
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /topics` : Create a new topic.
     *
     * @param topicDTO the topicDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new topicDTO, or with status `400 (Bad Request)` if the topic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/topics")
    fun createTopic(@Valid @RequestBody topicDTO: TopicDTO): ResponseEntity<TopicDTO> {
        log.debug("REST request to save Topic : {}", topicDTO)
        if (topicDTO.id != null) {
            throw BadRequestAlertException(
                "A new topic cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = topicService.save(topicDTO)
        return ResponseEntity.created(URI("/api/topics/" + result.id))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id.toString()))
            .body(result)
    }

    /**
     * `PUT  /topics` : Updates an existing topic.
     *
     * @param topicDTO the topicDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated topicDTO,
     * or with status `400 (Bad Request)` if the topicDTO is not valid,
     * or with status `500 (Internal Server Error)` if the topicDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/topics")
    fun updateTopic(@Valid @RequestBody topicDTO: TopicDTO): ResponseEntity<TopicDTO> {
        log.debug("REST request to update Topic : {}", topicDTO)
        if (topicDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = topicService.save(topicDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                    topicDTO.id.toString()
                )
            )
            .body(result)
    }

    /**
     * `GET  /topics` : get all the topics.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of topics in body.
     */
    @GetMapping("/topics")
    fun getAllTopics(
        pageable: Pageable
    ): ResponseEntity<MutableList<TopicDTO>> {
        log.debug("REST request to get a page of Topics")
        val page = topicService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /topics/:id` : get the "id" topic.
     *
     * @param id the id of the topicDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the topicDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/topics/{id}")
    fun getTopic(@PathVariable id: String): ResponseEntity<TopicDTO> {
        log.debug("REST request to get Topic : {}", id)
        val topicDTO = topicService.findOne(id)
        return ResponseUtil.wrapOrNotFound(topicDTO)
    }

    /**
     *  `DELETE  /topics/:id` : delete the "id" topic.
     *
     * @param id the id of the topicDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/topics/{id}")
    fun deleteTopic(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Topic : {}", id)
        topicService.delete(id)
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
    }
}
