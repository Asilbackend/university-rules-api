package uz.tuit.unirules.mapper.test;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uz.tuit.unirules.dto.respond_dto.QuestionRespDto;
import uz.tuit.unirules.entity.test.Question;
import uz.tuit.unirules.entity.test.QuestionOption;
import uz.tuit.unirules.entity.test.Test;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Named(value = "testId")
    static Long testId(Test test) {
        return test == null ? null : test.getId();
    }
    @Named(value = "findQuestionIds")
    static List<Long> findQuestionIds(List<QuestionOption> options) {
        if(options == null)    return null;
        return  options.stream().map(QuestionOption::getId).toList();
    }
}
