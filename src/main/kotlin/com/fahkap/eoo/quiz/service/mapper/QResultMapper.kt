package com.fahkap.eoo.quiz.service.mapper

import com.fahkap.eoo.quiz.domain.QResult
import com.fahkap.eoo.quiz.service.dto.QResultDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [QResult] and its DTO [QResultDTO].
 */
@Mapper(componentModel = "spring", uses = [QuestionMapper::class])
interface QResultMapper :
    EntityMapper<QResultDTO, QResult> {

    @Mappings(
        Mapping(source = "question.id", target = "questionId")
    )
    override fun toDto(qResult: QResult): QResultDTO

    @Mappings(
        Mapping(source = "questionId", target = "question")
    )
    override fun toEntity(qResultDTO: QResultDTO): QResult

    @JvmDefault
    fun fromId(id: String?) = id?.let {
        val qResult = QResult()
        qResult.id = id
        qResult
    }
}
