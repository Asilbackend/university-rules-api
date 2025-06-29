package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.tuit.unirules.entity.certificate.Certificate;
import uz.tuit.unirules.projections.CertificateProjection;

import java.util.List;

public interface CertificateRepository extends JpaRepository<Certificate,Long> {
    @Query(value = """
            SELECT 
            c.attachment_id AS attachmentId,
            m.name AS certificateName
            FROM certificate c 
            LEFT JOIN user_test ut ON c.user_test_id = ut.id
            LEFT JOIN users us ON c.user_id = us.id
            LEFT JOIN test t ON t.id = ut.test_id
            LEFT JOIN module m ON t.module_id = m.id
            WHERE us.id =:id
            ORDER BY c.created_at DESC LIMIT 3    
            """,nativeQuery = true)
   List<CertificateProjection> findCertificatesByUserId(@Param(value = "id")Long userId);
}
