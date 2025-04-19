package uz.tuit.unirules.repository.certificate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tuit.unirules.entity.certificate.Certificate;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate,Long> {
}
