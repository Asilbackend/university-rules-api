package uz.tuit.unirules.services.faculty;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.faculty.CreateEducationDirectionReqDto;
import uz.tuit.unirules.dto.request_dto.faculty.UpdateEducationDirectionReqDto;
import uz.tuit.unirules.dto.respond_dto.faculty.EducationDirectionRespDto;
import uz.tuit.unirules.entity.faculty.Faculty;
import uz.tuit.unirules.entity.faculty.education_direction.EducationDirection;
import uz.tuit.unirules.mapper.faculty.EducationDirectionMapper;
import uz.tuit.unirules.repository.faculty.EducationDirectionRepository;
import uz.tuit.unirules.repository.faculty.FacultyRepository;

import java.util.List;

@Service
public class EducationDirectionService {
    private final EducationDirectionRepository educationDirectionRepository;
    private final EducationDirectionMapper educationDirectionMapper;
    private final FacultyService facultyService;

    public EducationDirectionService(EducationDirectionRepository educationDirectionRepository,
                                     EducationDirectionMapper educationDirectionMapper, FacultyRepository facultyRepository, FacultyService facultyService) {
        this.educationDirectionRepository = educationDirectionRepository;
        this.educationDirectionMapper = educationDirectionMapper;
        this.facultyService = facultyService;
    }

    @Transactional
    public ApiResponse<EducationDirectionRespDto> create(CreateEducationDirectionReqDto createEducationReqDto) {
        // todo: Facultyni olish
        Faculty faculty = facultyService.findById(createEducationReqDto.facultyId());
        if (educationDirectionRepository.findByName(createEducationReqDto.name()).isEmpty()) {
            EducationDirection educationDirection = EducationDirection.builder()
                    .name(createEducationReqDto.name())
                    .faculty(faculty)
                    .build();
            educationDirectionRepository.save(educationDirection);
            return new ApiResponse<>(
                    201,
                    "%s education direction is created  successfully".formatted(educationDirection.getName()),
                    true,
                    educationDirectionMapper.toDto(educationDirection)
            );
        }
        throw new RuntimeException("This Education direction is already exist");
    }

    public ApiResponse<EducationDirectionRespDto> get(Long entityId) {
        EducationDirection educationDirection = findById(entityId);
        return new ApiResponse<>(
                200,
                "Education direction is found successfully by id = %s".formatted(entityId),
                true,
                educationDirectionMapper.toDto(educationDirection)
        );
    }

    public EducationDirection findById(Long entityId) {
        return educationDirectionRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("bunday education direction topilmadi"));

    }

    @Transactional
    public ApiResponse<EducationDirectionRespDto> update(Long entityId,
                                                         UpdateEducationDirectionReqDto updateEducationReqDto) {
        EducationDirection educationDirection = findById(entityId);
        Faculty faculty = facultyService.findById(updateEducationReqDto.facultyId());
        educationDirection.setName(updateEducationReqDto.name());
        educationDirection.setFaculty(faculty);
        educationDirectionRepository.save(educationDirection);
        return new ApiResponse<>(
                200,
                "educatioin direction muvaffaqiyatli update boldi",
                true,
                educationDirectionMapper.toDto(educationDirection)
        );
    }

    @Transactional
    public ApiResponse<EducationDirectionRespDto> delete(Long entityId) {
        educationDirectionRepository.delete(findById(entityId));
        return new ApiResponse<>(
                200,
                "education direction muvaffaqiyatli delete boldi",
                true,
                null
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<List<EducationDirectionRespDto>> getAll() {
        return new ApiResponse<>(
                200,
                "hamma education directions",
                true,
                educationDirectionRepository.findAll().stream().map(educationDirectionMapper::toDto).toList()
        );
    }

    @Transactional(readOnly = true)
    public ApiResponse<Page<EducationDirectionRespDto>> getAllPagination(Pageable pageable) {
        Page<EducationDirection> educationDirectionPage = educationDirectionRepository.findAllByIsDeletedFalse(pageable);
        Page<EducationDirectionRespDto> dtoPage = educationDirectionPage.map(educationDirectionMapper::toDto);
        return new ApiResponse<>(
                200,
                "hamma education directions page",
                true,
                dtoPage
        );
    }

    public Page<EducationDirection> findAllPage(Pageable pageable) {
        return educationDirectionRepository.findAll(pageable);
    }

}
