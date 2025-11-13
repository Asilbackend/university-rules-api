package uz.tuit.unirules.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.dto.respond_dto.UserImageDto;
import uz.tuit.unirules.services.user_image.UserImageService;


@RestController
@RequestMapping("/api/userImage")
@RequiredArgsConstructor
public class UserImageController {
    private final UserImageService userImageService;

    @GetMapping("/getLastImg")
    public HttpEntity<?> getUserLastImage() {
        ApiResponse<UserImageDto> apiResponse
                = new ApiResponse<>(200, "success", true,
                userImageService.getUserLastImage());
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping
    public HttpEntity<?> saveUserImage(@RequestParam Long attachmentId) {
        userImageService.saveUserImage(attachmentId);
        return ResponseEntity.noContent().build();
    }
}
