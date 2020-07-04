package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.service.QuestionService
import com.fahkap.eoo.quiz.service.dto.QuestionDTO
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

private const val ENTITY_NAME = "eooQuizQuestion"
/**
 * REST controller for managing [com.fahkap.eoo.quiz.domain.Question].
 */
@RestController
@RequestMapping("/api")
class QuestionResource(
    private val questionService: QuestionService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /questions` : Create a new question.
     *
     * @param questionDTO the questionDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new questionDTO, or with status `400 (Bad Request)` if the question has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questions")
    fun createQuestion(@Valid @RequestBody questionDTO: QuestionDTO): ResponseEntity<QuestionDTO> {
        log.debug("REST request to save Question : {}", questionDTO)
        if (questionDTO.id != null) {
            throw BadRequestAlertException(
                "A new question cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = questionService.save(questionDTO)
        return ResponseEntity.created(URI("/api/questions/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id))
            .body(result)
    }

    /**
     * `PUT  /questions` : Updates an existing question.
     *
     * @param questionDTO the questionDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated questionDTO,
     * or with status `400 (Bad Request)` if the questionDTO is not valid,
     * or with status `500 (Internal Server Error)` if the questionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questions")
    fun updateQuestion(@Valid @RequestBody questionDTO: QuestionDTO): ResponseEntity<QuestionDTO> {
        log.debug("REST request to update Question : {}", questionDTO)
        if (questionDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = questionService.save(questionDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     questionDTO.id
                )
            )
            .body(result)
    }
    /**
     * `GET  /questions` : get all the questions.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of questions in body.
     */
    @GetMapping("/questions")
    fun getAllQuestions(pageable: Pageable): ResponseEntity<List<QuestionDTO>> {
        log.debug("REST request to get a page of Questions")
        val page = questionService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /questions/:id` : get the "id" question.
     *
     * @param id the id of the questionDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the questionDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/questions/{id}")
    fun getQuestion(@PathVariable id: String): ResponseEntity<QuestionDTO> {
        log.debug("REST request to get Question : {}", id)
        val questionDTO = questionService.findOne(id)
        return ResponseUtil.wrapOrNotFound(questionDTO)
    }
    /**
     *  `DELETE  /questions/:id` : delete the "id" question.
     *
     * @param id the id of the questionDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/questions/{id}")
    fun deleteQuestion(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Question : {}", id)

        questionService.delete(id)
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
    }
}
