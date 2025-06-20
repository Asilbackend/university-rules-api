package uz.tuit.unirules.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.tuit.unirules.dto.ApiResponse;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.services.attachment.AttachmentService;

import java.io.IOException;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {
    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping(value = "/saveVideoWithPoster", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<Attachment> saveVideo(@RequestParam("multipartFile") MultipartFile multipartFile) {
        try {
            return new ApiResponse<>(200,
                    "successfully saved video + poster",
                    true,
                    attachmentService.saveVideoWithPoster(multipartFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<Attachment> saveFiles(@RequestParam("multipartFile") MultipartFile multipartFile) {
        return new ApiResponse<>(200,
                "successfully saved",
                true,
                attachmentService.saveFile(multipartFile));
    }

    @GetMapping("/{id}")
    public Attachment getAttachment(@PathVariable Long id) {
        return attachmentService.findById(id);
    }

    @GetMapping("/images/{imageName}")
    public HttpEntity<?> getImage(@PathVariable String imageName) throws RuntimeException {
        return attachmentService.getImageByName(imageName);
    }

    @GetMapping("/videos/{videoName}")
    public HttpEntity<?> getVideo(@PathVariable String videoName) {
        return attachmentService.getVideoByName(videoName);
    }

    @GetMapping("/audios/{audioName}")
    public HttpEntity<?> getAudio(@PathVariable String audioName) {
        return attachmentService.getAudioByName(audioName);
    }

    @GetMapping("/documents/{documentName}")
    public HttpEntity<?> getDocument(@PathVariable String documentName) {
        return attachmentService.getDocumentByName(documentName);
    }

    @GetMapping("/otherFiles/{fileName}")
    public HttpEntity<?> getOtherFile(@PathVariable String fileName) {
        return attachmentService.getOtherFile(fileName);
    }

    @DeleteMapping("/delete/{attachId}")
    public void deleteFile(@PathVariable Long attachId) {
        attachmentService.deleteFileByAttachId(attachId);
    }
}
