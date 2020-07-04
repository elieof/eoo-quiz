package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.service.QuizService
import com.fahkap.eoo.quiz.service.dto.QuizDTO
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

private const val ENTITY_NAME = "eooQuizQuiz"
/**
 * REST controller for managing [com.fahkap.eoo.quiz.domain.Quiz].
 */
@RestController
@RequestMapping("/api")
class QuizResource(
    private val quizService: QuizService
) {

    private val log = LoggerFactory.getLogger(javaClass)
    @Value("\${jhipster.clientApp.name}")
    private var applicationName: String? = null

    /**
     * `POST  /quizzes` : Create a new quiz.
     *
     * @param quizDTO the quizDTO to create.
     * @return the [ResponseEntity] with status `201 (Created)` and with body the new quizDTO, or with status `400 (Bad Request)` if the quiz has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/quizzes")
    fun createQuiz(@Valid @RequestBody quizDTO: QuizDTO): ResponseEntity<QuizDTO> {
        log.debug("REST request to save Quiz : {}", quizDTO)
        if (quizDTO.id != null) {
            throw BadRequestAlertException(
                "A new quiz cannot already have an ID",
                ENTITY_NAME, "idexists"
            )
        }
        val result = quizService.save(quizDTO)
        return ResponseEntity.created(URI("/api/quizzes/${result.id}"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.id))
            .body(result)
    }

    /**
     * `PUT  /quizzes` : Updates an existing quiz.
     *
     * @param quizDTO the quizDTO to update.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the updated quizDTO,
     * or with status `400 (Bad Request)` if the quizDTO is not valid,
     * or with status `500 (Internal Server Error)` if the quizDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/quizzes")
    fun updateQuiz(@Valid @RequestBody quizDTO: QuizDTO): ResponseEntity<QuizDTO> {
        log.debug("REST request to update Quiz : {}", quizDTO)
        if (quizDTO.id == null) {
            throw BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull")
        }
        val result = quizService.save(quizDTO)
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName, true, ENTITY_NAME,
                     quizDTO.id
                )
            )
            .body(result)
    }
    /**
     * `GET  /quizzes` : get all the quizzes.
     *
     * @param pageable the pagination information.

     * @return the [ResponseEntity] with status `200 (OK)` and the list of quizzes in body.
     */
    @GetMapping("/quizzes")
    fun getAllQuizzes(pageable: Pageable): ResponseEntity<List<QuizDTO>> {
        log.debug("REST request to get a page of Quizzes")
        val page = quizService.findAll(pageable)
        val headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page)
        return ResponseEntity.ok().headers(headers).body(page.content)
    }

    /**
     * `GET  /quizzes/:id` : get the "id" quiz.
     *
     * @param id the id of the quizDTO to retrieve.
     * @return the [ResponseEntity] with status `200 (OK)` and with body the quizDTO, or with status `404 (Not Found)`.
     */
    @GetMapping("/quizzes/{id}")
    fun getQuiz(@PathVariable id: String): ResponseEntity<QuizDTO> {
        log.debug("REST request to get Quiz : {}", id)
        val quizDTO = quizService.findOne(id)
        return ResponseUtil.wrapOrNotFound(quizDTO)
    }
    /**
     *  `DELETE  /quizzes/:id` : delete the "id" quiz.
     *
     * @param id the id of the quizDTO to delete.
     * @return the [ResponseEntity] with status `204 (NO_CONTENT)`.
     */
    @DeleteMapping("/quizzes/{id}")
    fun deleteQuiz(@PathVariable id: String): ResponseEntity<Void> {
        log.debug("REST request to delete Quiz : {}", id)

        quizService.delete(id)
            return ResponseEntity.noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id)).build()
    }
}
