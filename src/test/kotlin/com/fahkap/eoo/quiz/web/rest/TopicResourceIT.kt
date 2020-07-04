package com.fahkap.eoo.quiz.web.rest

import com.fahkap.eoo.quiz.EooQuizApp
import com.fahkap.eoo.quiz.config.SecurityBeanOverrideConfiguration
import com.fahkap.eoo.quiz.domain.Topic
import com.fahkap.eoo.quiz.repository.TopicRepository
import com.fahkap.eoo.quiz.service.TopicService
import com.fahkap.eoo.quiz.service.mapper.TopicMapper
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
 * Integration tests for the [TopicResource] REST controller.
 *
 * @see TopicResource
 */
@SpringBootTest(classes = [SecurityBeanOverrideConfiguration::class, EooQuizApp::class])
@AutoConfigureMockMvc
@WithMockUser
class TopicResourceIT {

    @Autowired
    private lateinit var topicRepository: TopicRepository

    @Autowired
    private lateinit var topicMapper: TopicMapper

    @Autowired
    private lateinit var topicService: TopicService

    @Autowired
    private lateinit var jacksonMessageConverter: MappingJackson2HttpMessageConverter

    @Autowired
    private lateinit var pageableArgumentResolver: PageableHandlerMethodArgumentResolver

    @Autowired
    private lateinit var exceptionTranslator: ExceptionTranslator

    @Autowired
    private lateinit var validator: Validator

    private lateinit var restTopicMockMvc: MockMvc

    private lateinit var topic: Topic

    @BeforeEach
    fun setup() {
        MockitoAnnotations.initMocks(this)
        val topicResource = TopicResource(topicService)
         this.restTopicMockMvc = MockMvcBuilders.standaloneSetup(topicResource)
             .setCustomArgumentResolvers(pageableArgumentResolver)
             .setControllerAdvice(exceptionTranslator)
             .setConversionService(createFormattingConversionService())
             .setMessageConverters(jacksonMessageConverter)
             .setValidator(validator).build()
    }

    @BeforeEach
    fun initTest() {
        topicRepository.deleteAll()
        topic = createEntity()
    }

    @Test
    @Throws(Exception::class)
    fun createTopic() {
        val databaseSizeBeforeCreate = topicRepository.findAll().size

        // Create the Topic
        val topicDTO = topicMapper.toDto(topic)
        restTopicMockMvc.perform(
            post("/api/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(topicDTO))
        ).andExpect(status().isCreated)

        // Validate the Topic in the database
        val topicList = topicRepository.findAll()
        assertThat(topicList).hasSize(databaseSizeBeforeCreate + 1)
        val testTopic = topicList[topicList.size - 1]
        assertThat(testTopic.name).isEqualTo(DEFAULT_NAME)
    }

    @Test
    fun createTopicWithExistingId() {
        val databaseSizeBeforeCreate = topicRepository.findAll().size

        // Create the Topic with an existing ID
        topic.id = "existing_id"
        val topicDTO = topicMapper.toDto(topic)

        // An entity with an existing ID cannot be created, so this API call must fail
        restTopicMockMvc.perform(
            post("/api/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(topicDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Topic in the database
        val topicList = topicRepository.findAll()
        assertThat(topicList).hasSize(databaseSizeBeforeCreate)
    }

    @Test
    fun checkNameIsRequired() {
        val databaseSizeBeforeTest = topicRepository.findAll().size
        // set the field null
        topic.name = null

        // Create the Topic, which fails.
        val topicDTO = topicMapper.toDto(topic)

        restTopicMockMvc.perform(
            post("/api/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(topicDTO))
        ).andExpect(status().isBadRequest)

        val topicList = topicRepository.findAll()
        assertThat(topicList).hasSize(databaseSizeBeforeTest)
    }

    @Test
    @Throws(Exception::class)
    fun getAllTopics() {
        // Initialize the database
        topicRepository.save(topic)

        // Get all the topicList
        restTopicMockMvc.perform(get("/api/topics?sort=id,desc"))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(topic.id)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME))) }

    @Test
    @Throws(Exception::class)
    fun getTopic() {
        // Initialize the database
        topicRepository.save(topic)

        val id = topic.id
        assertNotNull(id)

        // Get the topic
        restTopicMockMvc.perform(get("/api/topics/{id}", id))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(topic.id))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME)) }

    @Test
    @Throws(Exception::class)
    fun getNonExistingTopic() {
        // Get the topic
        restTopicMockMvc.perform(get("/api/topics/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound)
    }
    @Test
    fun updateTopic() {
        // Initialize the database
        topicRepository.save(topic)

        val databaseSizeBeforeUpdate = topicRepository.findAll().size

        // Update the topic
        val id = topic.id
        assertNotNull(id)
        val updatedTopic = topicRepository.findById(id).get()
        updatedTopic.name = UPDATED_NAME
        val topicDTO = topicMapper.toDto(updatedTopic)

        restTopicMockMvc.perform(
            put("/api/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(topicDTO))
        ).andExpect(status().isOk)

        // Validate the Topic in the database
        val topicList = topicRepository.findAll()
        assertThat(topicList).hasSize(databaseSizeBeforeUpdate)
        val testTopic = topicList[topicList.size - 1]
        assertThat(testTopic.name).isEqualTo(UPDATED_NAME)
    }

    @Test
    fun updateNonExistingTopic() {
        val databaseSizeBeforeUpdate = topicRepository.findAll().size

        // Create the Topic
        val topicDTO = topicMapper.toDto(topic)

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTopicMockMvc.perform(
            put("/api/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(convertObjectToJsonBytes(topicDTO))
        ).andExpect(status().isBadRequest)

        // Validate the Topic in the database
        val topicList = topicRepository.findAll()
        assertThat(topicList).hasSize(databaseSizeBeforeUpdate)
    }

    @Test
    @Throws(Exception::class)
    fun deleteTopic() {
        // Initialize the database
        topicRepository.save(topic)

        val databaseSizeBeforeDelete = topicRepository.findAll().size

        // Delete the topic
        restTopicMockMvc.perform(
            delete("/api/topics/{id}", topic.id)
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNoContent)

        // Validate the database contains one less item
        val topicList = topicRepository.findAll()
        assertThat(topicList).hasSize(databaseSizeBeforeDelete - 1)
    }

    companion object {

        private const val DEFAULT_NAME = "AAAAAAAAAA"
        private const val UPDATED_NAME = "BBBBBBBBBB"

        /**
         * Create an entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createEntity(): Topic {
            val topic = Topic(
                name = DEFAULT_NAME
            )

            return topic
        }

        /**
         * Create an updated entity for this test.
         *
         * This is a static method, as tests for other entities might also need it,
         * if they test an entity which requires the current entity.
         */
        @JvmStatic
        fun createUpdatedEntity(): Topic {
            val topic = Topic(
                name = UPDATED_NAME
            )

            return topic
        }
    }
}
