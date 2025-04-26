package uz.tuit.unirules.dto;


import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SimpleCrud  <ID,CreateReqDto,UpdateReqDto,RespDto>{
    @Transactional
    ApiResponse<RespDto> create(CreateReqDto dto);
    ApiResponse<RespDto> get(ID entityId);
    @Transactional
    ApiResponse<RespDto> update(ID entityId,UpdateReqDto dto);
    @Transactional
    ApiResponse<RespDto> delete(ID entityId);
    @Transactional(readOnly = true)
    ApiResponse<List<RespDto>> getAll();
    @Transactional(readOnly = true)
    ApiResponse<List<RespDto>> getAllPagination(Pageable pageable);
}