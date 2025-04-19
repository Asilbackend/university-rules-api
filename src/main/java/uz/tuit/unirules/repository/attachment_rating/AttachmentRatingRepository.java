package uz.tuit.unirules.repository.attachment_rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tuit.unirules.entity.attachment_rating.AttachmentRating;

@Repository
public interface AttachmentRatingRepository extends JpaRepository<AttachmentRating,Long> {
}
