package com.fahkap.eoo.quiz.repository

import com.fahkap.eoo.quiz.domain.QResult
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [QResult] entity.
 */
@Suppress("unused")
@Repository
interface QResultRepository : MongoRepository<QResult, String>
