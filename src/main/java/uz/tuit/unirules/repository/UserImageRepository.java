package uz.tuit.unirules.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.tuit.unirules.dto.respond_dto.UserImageDto;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.user_image.UserImage;

import java.util.List;
import java.util.Optional;

public interface UserImageRepository extends JpaRepository<UserImage, Long> {
    /*@Query("select ui from UserImage ui where ui.user.id=:userId and ui.deleted=false order by ui.id desc limit 1")
    Optional<UserImageDto> findByUserIdOrderByIdDescDeletedFalse(Long userId);*/

    @Query("select ui.attachment from UserImage ui where ui.user.id=:userId and ui.deleted=false order by ui.id desc limit 1")
    Optional<Attachment> findByUserIdOrderByIdDescDeletedFalse(Long userId);

    List<UserImage> findByUserIdAndDeletedFalse(Long id);
}