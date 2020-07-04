package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.EooQuizApp
import com.fahkap.eoo.quiz.config.SecurityBeanOverrideConfiguration
import com.fahkap.eoo.quiz.domain.Question
import com.fahkap.eoo.quiz.repository.QuestionRepository
import com.fahkap.eoo.quiz.service.QuestionService
import com.fahkap.eoo.quiz.service.mapper.QuestionMapper
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
 * Integration tests for the [QuestionResource] REST controller.
 *
 * @see QuestionResource
 */
@SpringBootTest(classes = [SecurityBeanOverrideConfiguration::class, EooQuizApp::class])
@AutoConfigureMockMvc
@WithMockUser
class QuestionResourceIT {

    @Autowired
    private lateinit var questionRepository: QuestionRepository

    @Autowired
    private lateinit var questionMapper: QuestionMapper

    @Autowired
    private lateinit var questionService: QuestionService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restQuestionMockMvc: MockMvc

    private lateinit var question: Question

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val questionResource = QuestionResource(questionService)
        this.restQuestionMockMvc = MockMvcBuilders.standaloneSetup(questionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        questionRepository.deleteAll()
        question = createEntity()
    }

    @Test
    @Throws(Exception::class)
    fun createQuestion() {
        val databaseSizeBeforeCreate = questionRepository.findAll().size

        // Create the Question
        val questionDTO = questionMapper.toDto(question)
        restQuestionMockMvc.perform(
            post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(questionDTO))
        ).andExpect(status().isCreated)

        // Validate the Question in the database
        val questionList = questionRepository.findAll()
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1)
        val testQuestion = questionList[questionList.size - 1]
        assertThat(testQuestion.statement).isEqualTo(DEFAULT_STATEMENT)
        assertThat(testQuestion.level).isEqualTo(DEFAULT_LEVEL)
    }

    @Test
    fun createQuestionWithExistingId() {
        val databaseSizeBeforeCreate = questionRepository.findAll().size

        // Create the Question with an existing ID
        question.id = "existing_id"
        val questionDTO = questionMapper.toDto(question)

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc.perform(
            post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(questionDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Question in the database
        val questionList = questionRepository.findAll()
        assertThat(questionList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    fun checkStatementIsRequired() {
        val databaseSizeBeforeTest = questionRepository.findAll().size
        // set the field null
        question.statement = null

        // Create the Question, which fails.
        val questionDTO = questionMapper.toDto(question)

        restQuestionMockMvc.perform(
            post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(questionDTO))
        ).andExpect(status().isBadRequest)

        val questionList = questionRepository.findAll()
        assertThat(questionList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    fun checkLevelIsRequired() {
        val databaseSizeBeforeTest = questionRepository.findAll().size
        // set the field null
        question.level = null

        // Create the Question, which fails.
        val questionDTO = questionMapper.toDto(question)

        restQuestionMockMvc.perform(
            post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(questionDTO))
        ).andExpect(status().isBadRequest)

        val questionList = questionRepository.findAll()
        assertThat(questionList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Throws(Exception::class)
    fun getAllQuestions() {
        // Initialize the database
        questionRepository.save(question)

        // Get all the questionList
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.id)))
            .andExpect(jsonPath("$.[*].statement").value(hasItem(DEFAULT_STATEMENT)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
    }

    @Test
    @Throws(Exception::class)
    fun getQuestion() {
        // Initialize the database
        questionRepository.save(question)

        val id = question.id
        assertNotNull(id)

        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(question.id))
            .andExpect(jsonPath("$.statement").value(DEFAULT_STATEMENT))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
    }

    @Test
    @Throws(Exception::class)
    fun getNonExistingQuestion() {
        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }

    @Test
    fun updateQuestion() {
        // Initialize the database
        questionRepository.save(question)

        val databaseSizeBeforeUpdate = questionRepository.findAll().size

        // Update the question
        val id = question.id
        assertNotNull(id)
        val updatedQuestion = questionRepository.findById(id).get()
        updatedQuestion.statement = UPDATED_STATEMENT
        updatedQuestion.level = UPDATED_LEVEL
        val questionDTO = questionMapper.toDto(updatedQuestion)

        restQuestionMockMvc.perform(
            put("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(questionDTO))
        ).andExpect(status().isOk)

        // Validate the Question in the database
        val questionList = questionRepository.findAll()
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate)
        val testQuestion = questionList[questionList.size - 1]
        assertThat(testQuestion.statement).isEqualTo(UPDATED_STATEMENT)
        assertThat(testQuestion.level).isEqualTo(UPDATED_LEVEL)
    }

    @Test
    fun updateNonExistingQuestion() {
        val databaseSizeBeforeUpdate = questionRepository.findAll().size

        // Create the Question
        val questionDTO = questionMapper.toDto(question)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc.perform(
            put("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(questionDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Question in the database
        val questionList = questionRepository.findAll()
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun deleteQuestion() {
        // Initialize the database
        questionRepository.save(question)

        val databaseSizeBeforeDelete = questionRepository.findAll().size

        // Delete the question
        restQuestionMockMvc.perform(
            delete("/api/questions/{id}", question.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val questionList = questionRepository.findAll()
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_STATEMENT = "AAAAAAAAAA"
        private const val UPDATED_STATEMENT = "BBBBBBBBBB"

        private const val DEFAULT_LEVEL: Int = 1
        private const val UPDATED_LEVEL: Int = 2

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Question {
            val question = Question(
                statement = DEFAULT_STATEMENT,
                level = DEFAULT_LEVEL
            )

            return question
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Question {
            val question = Question(
                statement = UPDATED_STATEMENT,
                level = UPDATED_LEVEL
            )

            return question
        }
    }
}
