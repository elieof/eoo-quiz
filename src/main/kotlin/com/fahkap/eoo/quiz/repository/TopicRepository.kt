package com.fahkap.eoo.quiz.repository

import com.fahkap.eoo.quiz.domain.Topic
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Topic] entity.
 */
@Suppress("unused")
@Repository
interface TopicRepository : MongoRepository<Topic, String>
