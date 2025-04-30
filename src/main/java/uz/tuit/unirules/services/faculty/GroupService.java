package uz.tuit.unirules.services.faculty;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.SimpleCrud;
import uz.tuit.unirules.dto.request_dto.faculty.CreateGroupReqDto;
import uz.tuit.unirules.dto.request_dto.faculty.UpdateGroupReqDto;
import uz.tuit.unirules.dto.respond_dto.faculty.GroupRespDto;
import uz.tuit.unirules.entity.faculty.group.Group;
import uz.tuit.unirules.mapper.faculty.GroupMapper;
import uz.tuit.unirules.repository.faculty.GroupRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService implements SimpleCrud<Long, CreateGroupReqDto, UpdateGroupReqDto, GroupRespDto> {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupService(GroupRepository groupRepository, GroupMapper groupMapper) {
        this.groupRepository = groupRepository;
        this.groupMapper = groupMapper;
    }

    @Override
    public ApiResponse<GroupRespDto> create(CreateGroupReqDto createGroupReqDto) {
        if (groupRepository.findByName(createGroupReqDto.name()).isEmpty()) {
            Group group = Group.builder()
                    .name(createGroupReqDto.name())
                    .build();
            groupRepository.save(group);
            return new ApiResponse<>(
                    201,
                    "Group muvafaqqiyatli yaratildi",
                    true,
                    groupMapper.toDto(group));
        } else {
            throw new RuntimeException(" Bu Group mavjud");
        }
    }

    @Override
    public ApiResponse<GroupRespDto> get(Long entityId) {
        Group group = findByGroupId(entityId);
        GroupRespDto respDto = groupMapper.toDto(group);
        return new ApiResponse<>(
                200,
                "Group topildi",
                true,
                respDto
        );
    }

    public Group findByGroupId(Long entityId) {
        Optional<Group> optionalGroup = groupRepository.findById(entityId);
        return optionalGroup.orElseThrow(() -> new EntityNotFoundException("group is not found"));
    }

    @Override
    public ApiResponse<GroupRespDto> update(Long entityId, UpdateGroupReqDto updateGroupReqDto) {
        Group group = findByGroupId(entityId);
        group.setName(updateGroupReqDto.name());
        groupRepository.save(group);
        return new ApiResponse<>(
                200,
                "Group muvaffaqiyatli update boldi",
                true,
                null
        );
    }

    @Override
    public ApiResponse<GroupRespDto> delete(Long entityId) {
        Group group = findByGroupId(entityId);
        groupRepository.delete(group);
        return new ApiResponse<>(
                200,
                "Group muvaffaqiyatli delete boldi",
                true,
                null
        );
    }

    @Override
    public ApiResponse<List<GroupRespDto>> getAll() {
        return new ApiResponse<>(
                200,
                "hamma grouplar",
                true,
                groupRepository.findAll().stream().map(groupMapper::toDto).toList()
        );
    }

    @Override
    public ApiResponse<List<GroupRespDto>> getAllPagination(Pageable pageable) {
        Page<Group> allPage= findAllPage(pageable);
        List<GroupRespDto> list=allPage.map(groupMapper::toDto).toList();
        return new ApiResponse<>(
                200,
                "hamma group pages",
                true,
                list
        );
    }
    public Page<Group> findAllPage(Pageable pageable){
        return groupRepository.findAll(pageable);
    }
}
