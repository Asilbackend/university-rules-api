package uz.tuit.unirules.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.support_request.SupportRequest;

import java.util.Optional;

public interface SupportRequestRepository extends JpaRepository<SupportRequest, Long> {

    Page<SupportRequest> findAllByUserIdAndIsDeletedFalse(Long studentId, Pageable pageable);

    Page<SupportRequest> findAllBySupportUserIdAndIsDeletedFalse(Long supportId, Pageable pageable);

    Page<SupportRequest> findAllByUserIdAndIsDeletedFalseAndStatus(Long studentId, SupportRequest.Status status, Pageable pageable);

    Page<SupportRequest>findAllBySupportUserIdAndIsDeletedFalseAndStatus(Long supportId, SupportRequest.Status status, Pageable pageable);
}
