package uz.tuit.unirules.services.certificate;

import org.springframework.stereotype.Service;
import uz.tuit.unirules.projections.CertificateProjection;
import uz.tuit.unirules.repository.CertificateRepository;
import uz.tuit.unirules.services.AuthUserService;

import java.util.List;

@Service
public class CertificateService {
    private final CertificateRepository certificateRepository;
    private final AuthUserService authUserService;

    public CertificateService(CertificateRepository certificateRepository, AuthUserService authUserService) {
        this.certificateRepository = certificateRepository;
        this.authUserService = authUserService;
    }

    public List<CertificateProjection> getLastThreeCertificates(){
        return certificateRepository.findCertificatesByUserId(authUserService.getAuthUserId());
    }
}
