package com.fahkap.eoo.quiz.repository

import com.fahkap.eoo.quiz.domain.Proposition
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

/**
 * Spring Data MongoDB repository for the [Proposition] entity.
 */
@Suppress("unused")
@Repository
interface PropositionRepository : MongoRepository<Proposition, String>
