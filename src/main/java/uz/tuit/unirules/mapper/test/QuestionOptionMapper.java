package uz.tuit.unirules.mapper.test;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uz.tuit.unirules.dto.respond_dto.QuestionOptionRespDto;
import uz.tuit.unirules.entity.test.Question;
import uz.tuit.unirules.entity.test.QuestionOption;

@Mapper(componentModel = "spring")
public interface QuestionOptionMapper {
    @Mapping(target = "questionOptionId", source = "id")
    @Mapping(target = "questionId", source = "question", qualifiedByName = "findQuestionId")
    QuestionOptionRespDto respDto(QuestionOption option);

    @Named(value = "findQuestionId")
    static Long findQuestionId(Question question) {
        return question == null ? null : question.getId();
    }
}
