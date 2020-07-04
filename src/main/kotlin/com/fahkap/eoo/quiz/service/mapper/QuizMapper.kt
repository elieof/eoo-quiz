package com.fahkap.eoo.quiz.service.mapper

import com.fahkap.eoo.quiz.domain.Quiz
import com.fahkap.eoo.quiz.service.dto.QuizDTO
import org.mapstruct.Mapper

/**
 * Mapper for the entity [Quiz] and its DTO [QuizDTO].
 */
@Mapper(componentModel = "spring", uses = [])
interface QuizMapper :
    EntityMapper<QuizDTO, Quiz> {

    override fun toEntity(quizDTO: QuizDTO): Quiz

    @JvmDefault
    fun fromId(id: String?) = id?.let {
        val quiz = Quiz()
        quiz.id = id
        quiz
    }
}
