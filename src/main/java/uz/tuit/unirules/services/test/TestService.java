package uz.tuit.unirules.services.test;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.request_dto.CreateTestReqDto;
import uz.tuit.unirules.dto.request_dto.UpdateTestReqDto;
import uz.tuit.unirules.dto.respond_dto.TestRespDto;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.entity.test.Test;
import uz.tuit.unirules.mapper.test.TestMapper;
import uz.tuit.unirules.repository.test.TestRepository;
import uz.tuit.unirules.services.module.ModuleService;

import java.util.List;
import java.util.Optional;

@Service
public class TestService {
    private final TestRepository testRepository;
    private final ModuleService moduleService;
    private final TestMapper testMapper;

    public TestService(TestRepository testRepository, ModuleService moduleService, TestMapper testMapper) {
        this.testRepository = testRepository;
        this.moduleService = moduleService;
        this.testMapper = testMapper;
    }

    @Transactional
    public ApiResponse<TestRespDto> create(CreateTestReqDto dto) {
        // moduleni olib kelish
        if (testRepository.findByModuleId(dto.moduleId()).isPresent()) {
            throw new RuntimeException("test already exist by moduleId=" + dto.moduleId());
        }
        Module module = moduleService.findById(dto.moduleId());
        Test test = Test.builder()
                .description(dto.description())
                .title(dto.title())
                .module(module)
                .durationSecond(dto.durationSecond())
                .build();
        testRepository.save(test);
        TestRespDto respDto = testMapper.toRespDto(test);

        return new ApiResponse<>(
                201,
                "saved",
                true,
                respDto
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<TestRespDto> get(Long id) {
        Test test = findByTestId(id);
        TestRespDto dto = testMapper.toRespDto(test);
        return new ApiResponse<>(
                200,
                "found",
                true,
                dto
        );
    }

    public Test findByTestId(Long testId) {
        return testRepository.findById(testId).orElseThrow(() ->
                new EntityNotFoundException("test is not found by this id = %s".formatted(testId)));
    }

    public Test findByTestIdAndIsDeletedFalse(Long testId) {
        Test test = testRepository.findById(testId).orElseThrow(() ->
                new EntityNotFoundException("test is not found by this id = %s".formatted(testId)));
        if (Boolean.TRUE.equals(test.getIsDeleted())) {
            throw new EntityNotFoundException("Test with id = %s is deleted".formatted(testId));
        }
        return test;
    }

    @Transactional
    public ApiResponse<TestRespDto> update(Long id, UpdateTestReqDto dto) {
        Test test = findByTestIdAndIsDeletedFalse(id);
        Optional<Test> optionalTest = testRepository.findByModuleId(dto.moduleId());
        if (optionalTest.isPresent()) {
            Test test1 = optionalTest.get();
            if (!test1.getId().equals(test.getId())) {
                throw new RuntimeException("test already exist by moduleId=" + dto.moduleId());
            }
        }
        Module module = moduleService.findById(dto.moduleId());
        test.setModule(module);
        test.setTitle(dto.title());
        testRepository.save(test);
        return new ApiResponse<>(
                200,
                "updated",
                true,
                null
        );
    }

    @Transactional
    public ApiResponse<TestRespDto> delete(Long id) {
        Test test = findByTestId(id);
        test.setIsDeleted(true);
        testRepository.save(test);
        return new ApiResponse<>(
                200,
                "deleted",
                true,
                null
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<TestRespDto>> getAll() {
        List<Test> tests = testRepository.findAllByIsDeletedFalse();
        List<TestRespDto> dto = tests.stream().map(testMapper::toRespDto).toList();
        return new ApiResponse<>(
                200,
                "all test",
                true,
                dto
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<Page<TestRespDto>> getAllPagination(Pageable pageable) {
        Page<Test> tests = testRepository.findAllByIsDeletedFalse(pageable);
        Page<TestRespDto> dto = tests.map(testMapper::toRespDto);
        return new ApiResponse<>(
                200,
                "all test",
                true,
                dto
        );
    }

    public Test findByModuleId(Long moduleId) {
        return testRepository.findByModuleId(moduleId).orElseThrow(() -> new EntityNotFoundException("Test topilmadi"));
    }

    @Transactional(readOnly = true)
    public ApiResponse<TestRespDto> getByModuleId(Long moduleId) {
        Test test = findByModuleId(moduleId);
        return get(test.getId());
    }
}
