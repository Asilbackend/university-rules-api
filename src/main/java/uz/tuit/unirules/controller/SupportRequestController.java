package uz.tuit.unirules.controller;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.respond_dto.SupportReqRespDto;
import uz.tuit.unirules.entity.support_request.SupportRequest;
import uz.tuit.unirules.services.support_request.SupportRequestService;

import java.util.List;

@RestController
@RequestMapping("/api/support_request")
public class SupportRequestController {
    private final SupportRequestService supportRequestService;

    public SupportRequestController(SupportRequestService supportRequestService) {
        this.supportRequestService = supportRequestService;
    }

    @PostMapping("/student/send-request")
    public HttpEntity<?> sendReq(@RequestParam Long userId, @RequestParam String subject, @RequestParam String message) {
        supportRequestService.sendReq(userId, subject, message);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/support/send-response")
    public HttpEntity<?> sendResp(@RequestParam Long supportUserId, @RequestParam Long supportReqId, @RequestParam String respMessage) {
        supportRequestService.sendResp(supportUserId, supportReqId, respMessage);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{supportReqId}")
    public HttpEntity<?> getById(@PathVariable Long supportReqId) {
        SupportReqRespDto supportReqRespDto = supportRequestService.getById(supportReqId);
        return ResponseEntity.ok(supportReqRespDto);
    }

    @GetMapping("/student/{supportReqId}")
    public HttpEntity<?> getByIdForStudent(@PathVariable Long supportReqId,
                                           @RequestParam Long studentId) {
        SupportReqRespDto supportReqRespDto = supportRequestService.getByIdForStudent(supportReqId, studentId);
        return ResponseEntity.ok(supportReqRespDto);
    }

    @GetMapping("/all")
    public HttpEntity<?> getAll() {
        List<SupportReqRespDto> supportReqRespDto = supportRequestService.getAll();
        return ResponseEntity.ok(supportReqRespDto);
    }

    @GetMapping
    public HttpEntity<?> getAllPage(@ParameterObject Pageable pageable) {
        supportRequestService.getAllPageable(pageable);
        return ResponseEntity.ok(supportRequestService.getAllPageable(pageable));
    }

    @GetMapping("/student/getAllByStudentId")
    public HttpEntity<?> getAllByStudentId(@RequestParam Long studentId,
                                           @RequestParam(required = false) SupportRequest.Status status,
                                           @ParameterObject Pageable pageable) {
        List<SupportReqRespDto> supportReqRespDtos = supportRequestService.getAllByStudentId(studentId, status, pageable);
        return ResponseEntity.ok(supportReqRespDtos);
    }

    @GetMapping("/support/getAllBySupportId")
    public HttpEntity<?> getAllBySupportId(@RequestParam Long supportId,
                                           @RequestParam(required = false) SupportRequest.Status status,
                                           @ParameterObject Pageable pageable) {
        List<SupportReqRespDto> supportReqRespDtos = supportRequestService.getAllBySupportId(supportId, status, pageable);
        return ResponseEntity.ok(supportReqRespDtos);
    }
}