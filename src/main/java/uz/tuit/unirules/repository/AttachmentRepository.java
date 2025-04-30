package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.entity.attachment.Attachment;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query("SELECT a.fileName FROM Attachment a WHERE a.fileName LIKE CONCAT(:prefix, '%')")
    List<String> findFilenamesByPrefix(String prefix);

    @Query("SELECT count(a.fileName) FROM Attachment a WHERE a.fileName LIKE CONCAT(:prefix, '%')")
    Integer countFilenamesByPrefix(String prefix);
}
