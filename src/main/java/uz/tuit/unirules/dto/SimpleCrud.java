package uz.tuit.unirules.dto;


import org.springframework.data.domain.Pageable;
import uz.tuit.unirules.dto.respond_dto.DisciplineRuleRespDto;

import java.util.List;

public interface SimpleCrud<ID, CreateReqDto, UpdateReqDto, RespDto> {
    ApiResponse<RespDto> create(CreateReqDto dto);

    ApiResponse<RespDto> get(ID entityId);

    ApiResponse<RespDto> update(ID entityId, UpdateReqDto dto);

    ApiResponse<RespDto> delete(ID entityId);

    ApiResponse<List<RespDto>> getAll();

    ApiResponse<List<RespDto>> getAllPagination(Pageable pageable);

}