package uz.tuit.unirules.services.faculty;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
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
public class EducationDirectionService implements
        SimpleCrud<Long, CreateEducationDirectionReqDto, UpdateEducationDirectionReqDto, EducationDirectionRespDto> {
    private final EducationDirectionRepository educationDirectionRepository;
    private final EducationDirectionMapper educationDirectionMapper;
    private final FacultyService facultyService;

    public EducationDirectionService(EducationDirectionRepository educationDirectionRepository,
                                     EducationDirectionMapper educationDirectionMapper, FacultyRepository facultyRepository, FacultyService facultyService) {
        this.educationDirectionRepository = educationDirectionRepository;
        this.educationDirectionMapper = educationDirectionMapper;
        this.facultyService = facultyService;
    }

    @Override
    public ApiResponse<EducationDirectionRespDto> create(CreateEducationDirectionReqDto createEducationReqDto) {
        // todo: Facultyni olish
        Faculty faculty = facultyService.findById(createEducationReqDto.facultyId());
        if (educationDirectionRepository.findByName(createEducationReqDto.name()).isEmpty()) {
            EducationDirection educationDirection = EducationDirection.builder()
                    .name(createEducationReqDto.name())
                    .build();
            educationDirectionRepository.save(educationDirection);
            return new ApiResponse<>(
                    201,
                    "Education Direction muvaffaqiyatli yaratildi",
                    true,
                    educationDirectionMapper.toDto(educationDirection)
            );
        }
        throw new RuntimeException("bunday education Direction mavjud");
    }

    @Override
    public ApiResponse<EducationDirectionRespDto> get(Long entityId) {
        EducationDirection educationDirection = findById(entityId);
        return new ApiResponse<>(
                200,
                "education direction muvaffaqiyatli topildi",
                true,
                educationDirectionMapper.toDto(educationDirection)
        );
    }

    public EducationDirection findById(Long entityId) {
        return educationDirectionRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("bunday education direction topilmadi"));

    }

    @Override
    public ApiResponse<EducationDirectionRespDto> update(Long entityId,
                                                         UpdateEducationDirectionReqDto updateEducationReqDto) {
        EducationDirection educationDirection = findById(entityId);
        educationDirection.setName(updateEducationReqDto.name());
        educationDirectionRepository.save(educationDirection);
        return new ApiResponse<>(
                200,
                "educatioin direction muvaffaqiyatli update boldi",
                true,
                educationDirectionMapper.toDto(educationDirection)
        );
    }

    @Override
    public ApiResponse<EducationDirectionRespDto> delete(Long entityId) {
        educationDirectionRepository.delete(findById(entityId));
        return new ApiResponse<>(
                200,
                "education direction muvaffaqiyatli delete boldi",
                true,
                null
        );
    }

    @Override
    public ApiResponse<List<EducationDirectionRespDto>> getAll() {
        return new ApiResponse<>(
                200,
                "hamma education directions",
                true,
                educationDirectionRepository.findAll().stream().map(educationDirectionMapper::toDto).toList()
        );
    }

    @Override
    public ApiResponse<List<EducationDirectionRespDto>> getAllPagination(Pageable pageable) {
        return new ApiResponse<>(
                200,
                "hamma education directions page",
                true,
                findAllPage(pageable).map(educationDirectionMapper::toDto).toList()
        );
    }

    public Page<EducationDirection> findAllPage(Pageable pageable) {
        return educationDirectionRepository.findAll(pageable);
    }

}
