package uz.tuit.unirules.services.user_image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.tuit.unirules.dto.respond_dto.UserImageDto;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.entity.user_image.UserImage;
import uz.tuit.unirules.repository.UserImageRepository;
import uz.tuit.unirules.services.AuthUserService;
import uz.tuit.unirules.services.attachment.AttachmentService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserImageService {
    private final AuthUserService authUserService;
    private final UserImageRepository userImageRepository;
    private final AttachmentService attachmentService;

    public UserImageDto getUserLastImage() {
        Optional<Attachment> attachment = userImageRepository.findByUserIdOrderByIdDescDeletedFalse(authUserService.getAuthUserId());
        if (attachment.isPresent()) {
            Attachment a = attachment.get();
            return new UserImageDto() {
                @Override
                public Long getAttachmentId() {
                    return a.getId();
                }

                @Override
                public String getImageUrl() {
                    return a.getUrl();
                }
            };
        }
        return null;
    }

    @Transactional
    public void saveUserImage(Long attachmentId) {
        Attachment attachment = attachmentService.findById(attachmentId);
        if (!attachment.getAttachType().equals(Attachment.AttachType.PICTURE)) {
            throw new IllegalArgumentException("attachment must be picture");
        }
        User authUser = authUserService.getAuthUser();
        deleteOldImages(authUser.getId());
        userImageRepository.save(new UserImage(authUser, attachment, false));
    }

    private void deleteOldImages(Long authUserId) {
        List<UserImage> userImages = userImageRepository.findByUserIdAndDeletedFalse(authUserId);
        userImages.forEach(userImage -> {
            userImage.setDeleted(true);
            userImageRepository.save(userImage);
        });
    }
}
