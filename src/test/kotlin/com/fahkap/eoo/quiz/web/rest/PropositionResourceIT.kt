package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.EooQuizApp
import com.fahkap.eoo.quiz.config.SecurityBeanOverrideConfiguration
import com.fahkap.eoo.quiz.domain.Proposition
import com.fahkap.eoo.quiz.repository.PropositionRepository
import com.fahkap.eoo.quiz.service.PropositionService
import com.fahkap.eoo.quiz.service.mapper.PropositionMapper
import com.fahkap.eoo.quiz.web.rest.errors.ExceptionTranslator
import kotlin.test.assertNotNull
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.validation.Validator

/**
 * Integration tests for the [PropositionResource] REST controller.
 *
 * @see PropositionResource
 */
@SpringBootTest(classes = [SecurityBeanOverrideConfiguration::class, EooQuizApp::class])
@AutoConfigureMockMvc
@WithMockUser
class PropositionResourceIT {

    @Autowired
    private lateinit var propositionRepository: PropositionRepository

    @Autowired
    private lateinit var propositionMapper: PropositionMapper

    @Autowired
    private lateinit var propositionService: PropositionService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restPropositionMockMvc: MockMvc

    private lateinit var proposition: Proposition

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val propositionResource = PropositionResource(propositionService)
         this.restPropositionMockMvc = MockMvcBuilders.standaloneSetup(propositionResource)
             .setCustomArgumentResolvers(pageableArgumentResolver)
             .setControllerAdvice(exceptionTranslator)
             .setConversionService(createFormattingConversionService())
             .setMessageConverters(jacksonMessageConverter)
             .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        propositionRepository.deleteAll()
        proposition = createEntity()
    }

    @Test
    @Throws(Exception::class)
    fun createProposition() {
        val databaseSizeBeforeCreate = propositionRepository.findAll().size

        // Create the Proposition
        val propositionDTO = propositionMapper.toDto(proposition)
        restPropositionMockMvc.perform(
            post("/api/propositions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(propositionDTO))
        ).andExpect(status().isCreated)

        // Validate the Proposition in the database
        val propositionList = propositionRepository.findAll()
        assertThat(propositionList).hasSize(databaseSizeBeforeCreate + 1)
        val testProposition = propositionList[propositionList.size - 1]
        assertThat(testProposition.statement).isEqualTo(DEFAULT_STATEMENT)
        assertThat(testProposition.valid).isEqualTo(DEFAULT_VALID)
        assertThat(testProposition.explanation).isEqualTo(DEFAULT_EXPLANATION)
    }

    @Test
    fun createPropositionWithExistingId() {
        val databaseSizeBeforeCreate = propositionRepository.findAll().size

        // Create the Proposition with an existing ID
        proposition.id = "existing_id"
        val propositionDTO = propositionMapper.toDto(proposition)

        // An entity with an existing ID cannot be created, so this API call must fail
        restPropositionMockMvc.perform(
            post("/api/propositions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(propositionDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Proposition in the database
        val propositionList = propositionRepository.findAll()
        assertThat(propositionList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    fun checkStatementIsRequired() {
        val databaseSizeBeforeTest = propositionRepository.findAll().size
        // set the field null
        proposition.statement = null

        // Create the Proposition, which fails.
        val propositionDTO = propositionMapper.toDto(proposition)

        restPropositionMockMvc.perform(
            post("/api/propositions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(propositionDTO))
        ).andExpect(status().isBadRequest)

        val propositionList = propositionRepository.findAll()
        assertThat(propositionList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun checkValidIsRequired() {
        val databaseSizeBeforeTest = propositionRepository.findAll().size
        // set the field null
        proposition.valid = null

        // Create the Proposition, which fails.
        val propositionDTO = propositionMapper.toDto(proposition)

        restPropositionMockMvc.perform(
            post("/api/propositions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(propositionDTO))
        ).andExpect(status().isBadRequest)

        val propositionList = propositionRepository.findAll()
        assertThat(propositionList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Throws(Exception::class)
    fun getAllPropositions() {
        // Initialize the database
        propositionRepository.save(proposition)

        // Get all the propositionList
        restPropositionMockMvc.perform(get("/api/propositions?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(proposition.id)))
            .andExpect(jsonPath("$.[*].statement").value(hasItem(DEFAULT_STATEMENT)))
            .andExpect(jsonPath("$.[*].valid").value(hasItem(DEFAULT_VALID)))
            .andExpect(jsonPath("$.[*].explanation").value(hasItem(DEFAULT_EXPLANATION))) }

    @Test
    @Throws(Exception::class)
    fun getProposition() {
        // Initialize the database
        propositionRepository.save(proposition)

        val id = proposition.id
        assertNotNull(id)

        // Get the proposition
        restPropositionMockMvc.perform(get("/api/propositions/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(proposition.id))
            .andExpect(jsonPath("$.statement").value(DEFAULT_STATEMENT))
            .andExpect(jsonPath("$.valid").value(DEFAULT_VALID))
            .andExpect(jsonPath("$.explanation").value(DEFAULT_EXPLANATION)) }

    @Test
    @Throws(Exception::class)
    fun getNonExistingProposition() {
        // Get the proposition
        restPropositionMockMvc.perform(get("/api/propositions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    fun updateProposition() {
        // Initialize the database
        propositionRepository.save(proposition)

        val databaseSizeBeforeUpdate = propositionRepository.findAll().size

        // Update the proposition
        val id = proposition.id
        assertNotNull(id)
        val updatedProposition = propositionRepository.findById(id).get()
        updatedProposition.statement = UPDATED_STATEMENT
        updatedProposition.valid = UPDATED_VALID
        updatedProposition.explanation = UPDATED_EXPLANATION
        val propositionDTO = propositionMapper.toDto(updatedProposition)

        restPropositionMockMvc.perform(
            put("/api/propositions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(propositionDTO))
        ).andExpect(status().isOk)

        // Validate the Proposition in the database
        val propositionList = propositionRepository.findAll()
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate)
        val testProposition = propositionList[propositionList.size - 1]
        assertThat(testProposition.statement).isEqualTo(UPDATED_STATEMENT)
        assertThat(testProposition.valid).isEqualTo(UPDATED_VALID)
        assertThat(testProposition.explanation).isEqualTo(UPDATED_EXPLANATION)
    }

    @Test
    fun updateNonExistingProposition() {
        val databaseSizeBeforeUpdate = propositionRepository.findAll().size

        // Create the Proposition
        val propositionDTO = propositionMapper.toDto(proposition)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPropositionMockMvc.perform(
            put("/api/propositions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(propositionDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Proposition in the database
        val propositionList = propositionRepository.findAll()
        assertThat(propositionList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun deleteProposition() {
        // Initialize the database
        propositionRepository.save(proposition)

        val databaseSizeBeforeDelete = propositionRepository.findAll().size

        // Delete the proposition
        restPropositionMockMvc.perform(
            delete("/api/propositions/{id}", proposition.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val propositionList = propositionRepository.findAll()
        assertThat(propositionList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_STATEMENT = "AAAAAAAAAA"
        private const val UPDATED_STATEMENT = "BBBBBBBBBB"

        private const val DEFAULT_VALID: Boolean = false
        private const val UPDATED_VALID: Boolean = true

        private const val DEFAULT_EXPLANATION = "AAAAAAAAAA"
        private const val UPDATED_EXPLANATION = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Proposition {
            val proposition = Proposition(
                statement = DEFAULT_STATEMENT,
                valid = DEFAULT_VALID,
                explanation = DEFAULT_EXPLANATION
            )

            return proposition
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Proposition {
            val proposition = Proposition(
                statement = UPDATED_STATEMENT,
                valid = UPDATED_VALID,
                explanation = UPDATED_EXPLANATION
            )

            return proposition
        }
    }
}
