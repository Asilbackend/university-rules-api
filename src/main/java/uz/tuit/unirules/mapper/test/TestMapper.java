package uz.tuit.unirules.mapper.test;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uz.tuit.unirules.dto.respond_dto.TestRespDto;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.entity.test.Question;
import uz.tuit.unirules.entity.test.Test;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TestMapper {
    @Mapping(source = "module",target = "moduleId",qualifiedByName = "moduleId")
    @Mapping(source = "questions",target = "questionIds",qualifiedByName = "findQuestionIds")
    @Mapping(source = "id",target = "testId")
    TestRespDto toRespDto(Test test);

    @Named(value = "moduleId")
    static Long moduleId(Module module){
        return module==null?null:module.getId();
    }
    @Named(value = "findQuestionIds")
    static List<Long> findQuestionIds(List<Question> questions){
        if (questions==null)return  null;
        return questions.stream().map(Question::getId).toList();
    }

}
