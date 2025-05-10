package uz.tuit.unirules.services.faculty;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.faculty.CreateFacultyReqDto;
import uz.tuit.unirules.dto.request_dto.faculty.UpdateFacultyReqDto;
import uz.tuit.unirules.dto.respond_dto.faculty.FacultyRespDto;
import uz.tuit.unirules.entity.faculty.Faculty;
import uz.tuit.unirules.mapper.faculty.FacultyMapper;
import uz.tuit.unirules.repository.faculty.FacultyRepository;

import java.util.List;

@Service
public class FacultyService implements
        SimpleCrud<Long, CreateFacultyReqDto, UpdateFacultyReqDto, FacultyRespDto> {
    private final FacultyMapper facultyMapper;
    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyMapper facultyMapper, FacultyRepository facultyRepository) {
        this.facultyMapper = facultyMapper;
        this.facultyRepository = facultyRepository;
    }

    @Override
    @Transactional
    public ApiResponse<FacultyRespDto> create(CreateFacultyReqDto createFacultyReqDto) {
        if (facultyRepository.findByName(createFacultyReqDto.name()).isEmpty()){
            Faculty faculty=Faculty.builder()
                    .name(createFacultyReqDto.name())
                    .description(createFacultyReqDto.description())
                    .build();
            facultyRepository.save(faculty);
            return new ApiResponse<>(
                    201,
                    "faculty yaratildi",
                    true,
                    facultyMapper.toDto(faculty)
            );
        }
       else {
           throw new RuntimeException("bunday faculty mavjud");
        }
    }

    @Override
    public ApiResponse<FacultyRespDto> get(Long entityId) {
        Faculty faculty=findById(entityId);
        return new ApiResponse<>(
                200,
                "faculty muvaffaqiyatli topildi",
                true,
                facultyMapper.toDto(faculty)
        );
    }
    public Faculty findById(Long entityId){
        return facultyRepository.findById(entityId)
                .orElseThrow(() -> new EntityNotFoundException("bunday faculty mavjud emas"));
    }

    @Override
    @Transactional
    public ApiResponse<FacultyRespDto> update(Long entityId, UpdateFacultyReqDto updateFacultyReqDto) {
        Faculty faculty=findById(entityId);
        faculty.setName(updateFacultyReqDto.name());
        faculty.setDescription(updateFacultyReqDto.description());
        facultyRepository.save(faculty);
        return new ApiResponse<>(
                200,
                "faculty muvaffaqiyatli update boldi",
                true,
                facultyMapper.toDto(faculty)
        );
    }

    @Override
    @Transactional
    public ApiResponse<FacultyRespDto> delete(Long entityId) {
        Faculty faculty=findById(entityId);
        facultyRepository.delete(faculty);
        return new ApiResponse<>(
                200,
                "faculty muvaffaqiyatli delete boldi",
                true,
                null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<FacultyRespDto>> getAll() {
        return new ApiResponse<>(
                200,
                " hamma faculty list",
                true,
                facultyRepository.findAll().stream().map(facultyMapper::toDto).toList()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<FacultyRespDto>> getAllPagination(Pageable pageable) {
        Page<Faculty> allPage=findAllPage(pageable);
        return new ApiResponse<>(
                200,
                "hamma page",
                true,
                allPage.map(facultyMapper::toDto).toList()
        );
    }
    public Page<Faculty> findAllPage(Pageable pageable){
        return facultyRepository.findAll(pageable);
    }
}
