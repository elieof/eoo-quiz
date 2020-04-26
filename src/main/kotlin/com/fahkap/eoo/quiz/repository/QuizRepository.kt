package com.fahkap.eoo.quiz.repository

import com.fahkap.eoo.quiz.domain.Quiz
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Quiz] entity.
 */
@Suppress("unused")
@Repository
interface QuizRepository : MongoRepository<Quiz, String>
