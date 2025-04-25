package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tuit.unirules.entity.attachment.Attachment;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

}
