package uz.tuit.unirules.repository.attachment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tuit.unirules.entity.attachment.Attachment;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
}
