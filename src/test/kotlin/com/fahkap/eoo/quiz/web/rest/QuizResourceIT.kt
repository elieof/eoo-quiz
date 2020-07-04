package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.EooQuizApp
import com.fahkap.eoo.quiz.config.SecurityBeanOverrideConfiguration
import com.fahkap.eoo.quiz.domain.Quiz
import com.fahkap.eoo.quiz.repository.QuizRepository
import com.fahkap.eoo.quiz.service.QuizService
import com.fahkap.eoo.quiz.service.mapper.QuizMapper
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
 * Integration tests for the [QuizResource] REST controller.
 *
 * @see QuizResource
 */
@SpringBootTest(classes = [SecurityBeanOverrideConfiguration::class, EooQuizApp::class])
@AutoConfigureMockMvc
@WithMockUser
class QuizResourceIT {

    @Autowired
    private lateinit var quizRepository: QuizRepository

    @Autowired
    private lateinit var quizMapper: QuizMapper

    @Autowired
    private lateinit var quizService: QuizService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restQuizMockMvc: MockMvc

    private lateinit var quiz: Quiz

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val quizResource = QuizResource(quizService)
        this.restQuizMockMvc = MockMvcBuilders.standaloneSetup(quizResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        quizRepository.deleteAll()
        quiz = createEntity()
    }

    @Test
    @Throws(Exception::class)
    fun createQuiz() {
        val databaseSizeBeforeCreate = quizRepository.findAll().size

        // Create the Quiz
        val quizDTO = quizMapper.toDto(quiz)
        restQuizMockMvc.perform(
            post("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(quizDTO))
        ).andExpect(status().isCreated)

        // Validate the Quiz in the database
        val quizList = quizRepository.findAll()
        assertThat(quizList).hasSize(databaseSizeBeforeCreate + 1)
        val testQuiz = quizList[quizList.size - 1]
        assertThat(testQuiz.name).isEqualTo(DEFAULT_NAME)
        assertThat(testQuiz.description).isEqualTo(DEFAULT_DESCRIPTION)
    }

    @Test
    fun createQuizWithExistingId() {
        val databaseSizeBeforeCreate = quizRepository.findAll().size

        // Create the Quiz with an existing ID
        quiz.id = "existing_id"
        val quizDTO = quizMapper.toDto(quiz)

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuizMockMvc.perform(
            post("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(quizDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Quiz in the database
        val quizList = quizRepository.findAll()
        assertThat(quizList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = quizRepository.findAll().size
        // set the field null
        quiz.name = null

        // Create the Quiz, which fails.
        val quizDTO = quizMapper.toDto(quiz)

        restQuizMockMvc.perform(
            post("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(quizDTO))
        ).andExpect(status().isBadRequest)

        val quizList = quizRepository.findAll()
        assertThat(quizList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Throws(Exception::class)
    fun getAllQuizzes() {
        // Initialize the database
        quizRepository.save(quiz)

        // Get all the quizList
        restQuizMockMvc.perform(get("/api/quizzes?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(quiz.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
    }

    @Test
    @Throws(Exception::class)
    fun getQuiz() {
        // Initialize the database
        quizRepository.save(quiz)

        val id = quiz.id
        assertNotNull(id)

        // Get the quiz
        restQuizMockMvc.perform(get("/api/quizzes/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(quiz.id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
    }

    @Test
    @Throws(Exception::class)
    fun getNonExistingQuiz() {
        // Get the quiz
        restQuizMockMvc.perform(get("/api/quizzes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateQuiz() {
        // Initialize the database
        quizRepository.save(quiz)

        val databaseSizeBeforeUpdate = quizRepository.findAll().size

        // Update the quiz
        val id = quiz.id
        assertNotNull(id)
        val updatedQuiz = quizRepository.findById(id).get()
        updatedQuiz.name = UPDATED_NAME
        updatedQuiz.description = UPDATED_DESCRIPTION
        val quizDTO = quizMapper.toDto(updatedQuiz)

        restQuizMockMvc.perform(
            put("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(quizDTO))
        ).andExpect(status().isOk)

        // Validate the Quiz in the database
        val quizList = quizRepository.findAll()
        assertThat(quizList).hasSize(databaseSizeBeforeUpdate)
        val testQuiz = quizList[quizList.size - 1]
        assertThat(testQuiz.name).isEqualTo(UPDATED_NAME)
        assertThat(testQuiz.description).isEqualTo(UPDATED_DESCRIPTION)
    }

    @Test
    fun updateNonExistingQuiz() {
        val databaseSizeBeforeUpdate = quizRepository.findAll().size

        // Create the Quiz
        val quizDTO = quizMapper.toDto(quiz)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuizMockMvc.perform(
            put("/api/quizzes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(quizDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Quiz in the database
        val quizList = quizRepository.findAll()
        assertThat(quizList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun deleteQuiz() {
        // Initialize the database
        quizRepository.save(quiz)

        val databaseSizeBeforeDelete = quizRepository.findAll().size

        // Delete the quiz
        restQuizMockMvc.perform(
            delete("/api/quizzes/{id}", quiz.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val quizList = quizRepository.findAll()
        assertThat(quizList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        private const val DEFAULT_DESCRIPTION = "AAAAAAAAAA"
        private const val UPDATED_DESCRIPTION = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Quiz {
            val quiz = Quiz(
                name = DEFAULT_NAME,
                description = DEFAULT_DESCRIPTION
            )

            return quiz
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Quiz {
            val quiz = Quiz(
                name = UPDATED_NAME,
                description = UPDATED_DESCRIPTION
            )

            return quiz
        }
    }
}
