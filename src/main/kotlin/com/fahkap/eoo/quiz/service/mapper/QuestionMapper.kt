package com.fahkap.eoo.quiz.service.mapper

import com.fahkap.eoo.quiz.domain.Question
import com.fahkap.eoo.quiz.service.dto.QuestionDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [Question] and its DTO [QuestionDTO].
 */
@Mapper(componentModel = "spring", uses = [TopicMapper::class, QuizMapper::class])
interface QuestionMapper :
    EntityMapper<QuestionDTO, Question> {

    @Mappings(
        Mapping(source = "topic.id", target = "topicId"),
        Mapping(source = "quiz.id", target = "quizId")
    )
    override fun toDto(question: Question): QuestionDTO

    @Mappings(
        Mapping(target = "propositions", ignore = true),
        Mapping(target = "removeProposition", ignore = true),
        Mapping(source = "topicId", target = "topic"),
        Mapping(source = "quizId", target = "quiz")
    )
    override fun toEntity(questionDTO: QuestionDTO): Question

    @JvmDefault
    fun fromId(id: String?) = id?.let {
        val question = Question()
        question.id = id
        question
    }
}
