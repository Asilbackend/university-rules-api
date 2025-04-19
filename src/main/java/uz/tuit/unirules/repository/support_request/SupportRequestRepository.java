package uz.tuit.unirules.repository.support_request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tuit.unirules.entity.support_request.SupportRequest;

@Repository
public interface SupportRequestRepository extends JpaRepository<SupportRequest,Long> {
}
