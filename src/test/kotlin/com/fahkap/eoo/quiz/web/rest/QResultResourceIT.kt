package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.EooQuizApp
import com.fahkap.eoo.quiz.config.SecurityBeanOverrideConfiguration
import com.fahkap.eoo.quiz.domain.QResult
import com.fahkap.eoo.quiz.repository.QResultRepository
import com.fahkap.eoo.quiz.service.QResultService
import com.fahkap.eoo.quiz.service.mapper.QResultMapper
import com.fahkap.eoo.quiz.web.rest.errors.ExceptionTranslator
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasItem
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.Validator
import kotlin.test.assertNotNull

/**
 * Integration tests for the [QResultResource] REST controller.
 *
 * @see QResultResource
 */
@SpringBootTest(classes = [SecurityBeanOverrideConfiguration::class, EooQuizApp::class])
@AutoConfigureMockMvc
@WithMockUser
class QResultResourceIT {

    @Autowired
    private lateinit var qResultRepository: QResultRepository

    @Autowired
    private lateinit var qResultMapper: QResultMapper

    @Autowired
    private lateinit var qResultService: QResultService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restQResultMockMvc: MockMvc

    private lateinit var qResult: QResult

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val qResultResource = QResultResource(qResultService)
        this.restQResultMockMvc = MockMvcBuilders.standaloneSetup(qResultResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        qResultRepository.deleteAll()
        qResult = createEntity()
    }

    @Test
    @Throws(Exception::class)
    fun createQResult() {
        val databaseSizeBeforeCreate = qResultRepository.findAll().size

        // Create the QResult
        val qResultDTO = qResultMapper.toDto(qResult)
        restQResultMockMvc.perform(
            post("/api/q-results")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(qResultDTO))
        ).andExpect(status().isCreated)

        // Validate the QResult in the database
        val qResultList = qResultRepository.findAll()
        assertThat(qResultList).hasSize(databaseSizeBeforeCreate + 1)
        val testQResult = qResultList[qResultList.size - 1]
        assertThat(testQResult.username).isEqualTo(DEFAULT_USERNAME)
        assertThat(testQResult.valid).isEqualTo(DEFAULT_VALID)
    }

    @Test
    fun createQResultWithExistingId() {
        val databaseSizeBeforeCreate = qResultRepository.findAll().size

        // Create the QResult with an existing ID
        qResult.id = "existing_id"
        val qResultDTO = qResultMapper.toDto(qResult)

        // An entity with an existing ID cannot be created, so this API call must fail
        restQResultMockMvc.perform(
            post("/api/q-results")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(qResultDTO))
        ).andExpect(status().isBadRequest)

        // Validate the QResult in the database
        val qResultList = qResultRepository.findAll()
        assertThat(qResultList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    fun checkValidIsRequired() {
        val databaseSizeBeforeTest = qResultRepository.findAll().size
        // set the field null
        qResult.valid = null

        // Create the QResult, which fails.
        val qResultDTO = qResultMapper.toDto(qResult)

        restQResultMockMvc.perform(
            post("/api/q-results")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(qResultDTO))
        ).andExpect(status().isBadRequest)

        val qResultList = qResultRepository.findAll()
        assertThat(qResultList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Throws(Exception::class)
    fun getAllQResults() {
        // Initialize the database
        qResultRepository.save(qResult)

        // Get all the qResultList
        restQResultMockMvc.perform(get("/api/q-results?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(qResult.id)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].valid").value(hasItem(DEFAULT_VALID)))
    }

    @Test
    @Throws(Exception::class)
    fun getQResult() {
        // Initialize the database
        qResultRepository.save(qResult)

        val id = qResult.id
        assertNotNull(id)

        // Get the qResult
        restQResultMockMvc.perform(get("/api/q-results/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(qResult.id))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.valid").value(DEFAULT_VALID))
    }

    @Test
    @Throws(Exception::class)
    fun getNonExistingQResult() {
        // Get the qResult
        restQResultMockMvc.perform(get("/api/q-results/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateQResult() {
        // Initialize the database
        qResultRepository.save(qResult)

        val databaseSizeBeforeUpdate = qResultRepository.findAll().size

        // Update the qResult
        val id = qResult.id
        assertNotNull(id)
        val updatedQResult = qResultRepository.findById(id).get()
        updatedQResult.username = UPDATED_USERNAME
        updatedQResult.valid = UPDATED_VALID
        val qResultDTO = qResultMapper.toDto(updatedQResult)

        restQResultMockMvc.perform(
            put("/api/q-results")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(qResultDTO))
        ).andExpect(status().isOk)

        // Validate the QResult in the database
        val qResultList = qResultRepository.findAll()
        assertThat(qResultList).hasSize(databaseSizeBeforeUpdate)
        val testQResult = qResultList[qResultList.size - 1]
        assertThat(testQResult.username).isEqualTo(UPDATED_USERNAME)
        assertThat(testQResult.valid).isEqualTo(UPDATED_VALID)
    }

    @Test
    fun updateNonExistingQResult() {
        val databaseSizeBeforeUpdate = qResultRepository.findAll().size

        // Create the QResult
        val qResultDTO = qResultMapper.toDto(qResult)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQResultMockMvc.perform(
            put("/api/q-results")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(qResultDTO))
        ).andExpect(status().isBadRequest)

        // Validate the QResult in the database
        val qResultList = qResultRepository.findAll()
        assertThat(qResultList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun deleteQResult() {
        // Initialize the database
        qResultRepository.save(qResult)

        val databaseSizeBeforeDelete = qResultRepository.findAll().size

        // Delete the qResult
        restQResultMockMvc.perform(
            delete("/api/q-results/{id}", qResult.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val qResultList = qResultRepository.findAll()
        assertThat(qResultList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_USERNAME = "AAAAAAAAAA"
        private const val UPDATED_USERNAME = "BBBBBBBBBB"

        private const val DEFAULT_VALID: Boolean = false
        private const val UPDATED_VALID: Boolean = true

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): QResult {
            val qResult = QResult(
                username = DEFAULT_USERNAME,
                valid = DEFAULT_VALID
            )

            return qResult
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): QResult {
            val qResult = QResult(
                username = UPDATED_USERNAME,
                valid = UPDATED_VALID
            )

            return qResult
        }
    }
}
