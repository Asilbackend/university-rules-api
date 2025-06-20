package uz.tuit.unirules.services.attachment_student;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.services.attachment.AttachmentService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class VideoService {
    private final AttachmentService attachmentService;

    public VideoService(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    public ResponseEntity<ResourceRegion> prepareVideoRegion(Long id, HttpHeaders headers) throws IOException {
        Attachment attachment = attachmentService.findById(id);
        String fileName = attachment.getFileName();
        Path videoPath = attachmentService.getVideoPath(fileName);
        FileSystemResource video = new FileSystemResource(videoPath);
        if (!video.exists()) {
            throw new FileNotFoundException("Video not found: " + id);
        }

        long contentLength = video.contentLength();
        ResourceRegion region = getRegion(video, headers, contentLength);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(video).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(region);
    }

    private ResourceRegion getRegion(Resource video, HttpHeaders headers, long contentLength) throws IOException {
        final long chunkSize = 1024 * 1024; // 1MB

        if (headers.getRange().isEmpty()) {
            long rangeLength = Math.min(chunkSize, contentLength);
            return new ResourceRegion(video, 0, rangeLength);
        } else {
            HttpRange range = headers.getRange().getFirst();
            long start = range.getRangeStart(contentLength);
            long end = range.getRangeEnd(contentLength);
            long rangeLength = Math.min(chunkSize, end - start + 1);
            return new ResourceRegion(video, start, rangeLength);
        }
    }
}
