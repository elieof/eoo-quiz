package com.fahkap.eoo.quiz.service.mapper

import com.fahkap.eoo.quiz.domain.Proposition
import com.fahkap.eoo.quiz.service.dto.PropositionDTO
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

/**
 * Mapper for the entity [Proposition] and its DTO [PropositionDTO].
 */
@Mapper(componentModel = "spring", uses = [QuestionMapper::class])
interface PropositionMapper :
    EntityMapper<PropositionDTO, Proposition> {

    @Mappings(
        Mapping(source = "question.id", target = "questionId")
    )
    override fun toDto(proposition: Proposition): PropositionDTO

    @Mappings(
        Mapping(source = "questionId", target = "question")
    )
    override fun toEntity(propositionDTO: PropositionDTO): Proposition

    @JvmDefault
    fun fromId(id: String?) = id?.let {
        val proposition = Proposition()
        proposition.id = id
        proposition
    }
}
