package com.fahkap.eoo.quiz.repository

import com.fahkap.eoo.quiz.domain.Question
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Question] entity.
 */
@Suppress("unused")
@Repository
interface QuestionRepository : MongoRepository<Question, String>
