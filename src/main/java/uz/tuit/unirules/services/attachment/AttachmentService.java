package uz.tuit.unirules.services.attachment;

import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.repository.AttachmentRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    // Map AttachType to MIME types
    private static final Map<Attachment.AttachType, String> MIME_TYPES = Map.of(
            Attachment.AttachType.PICTURE, "image/jpeg", // Adjust based on actual file extension if needed
            Attachment.AttachType.VIDEO, "video/mp4",
            Attachment.AttachType.AUDIO, "audio/mpeg",
            Attachment.AttachType.DOCUMENT, "application/pdf", // Default for documents
            Attachment.AttachType.ANY, MediaType.APPLICATION_OCTET_STREAM_VALUE
    );

    // Map AttachType to Content-Disposition
    private static final Map<Attachment.AttachType, String> DISPOSITION_TYPES = Map.of(
            Attachment.AttachType.PICTURE, "inline",
            Attachment.AttachType.VIDEO, "inline",
            Attachment.AttachType.AUDIO, "inline",
            Attachment.AttachType.DOCUMENT, "attachment",
            Attachment.AttachType.ANY, "attachment"
    );

    private static final Set<String> IMAGE_EXTENSIONS = Set.of(".jpeg", ".jpg", ".png", ".gif", ".webp");
    private static final Set<String> VIDEO_EXTENSIONS = Set.of(".mp4", ".webm", ".mov");
    private static final Set<String> AUDIO_EXTENSIONS = Set.of(".mp3", ".wav", ".ogg");
    private static final Set<String> DOCUMENT_EXTENSIONS = Set.of(".pdf", ".doc", ".docx", ".ppt", ".pptx", ".xls", ".xlsx", ".txt");

    @Value("${image.file}")
    private String imagePath;
    @Value("${video.file}")
    private String videoPath;
    @Value("${audio.file}")
    private String audioPath;
    @Value("${document.file}")
    private String documentPath;
    @Value("${other.file}")
    private String otherFilePath;

    @Value("${image.url}")
    private String imageUrl;
    @Value("${video.url}")
    private String videoUrl;
    @Value("${audio.url}")
    private String audioUrl;
    @Value("${document.url}")
    private String documentUrl;
    @Value("${other.url}")
    private String otherUrl;

    public Path getVideoPath(String fileName) {
        return Paths.get(videoPath + fileName);
    }

    @Getter
    @Setter
    private static class FileNameAndExtension {
        private String fileName;
        private String extension;

        FileNameAndExtension(String fileName, String extension) {
            this.fileName = fileName;
            this.extension = extension;
        }
    }

    @Transactional
    public Attachment saveFile(MultipartFile file) {
        FileNameAndExtension fileDetails = cleanFileName(file.getOriginalFilename());
        FileConfig fileConfig = determineFileConfig(fileDetails.extension);
        Path path = Paths.get(fileConfig.path + fileDetails.fileName);
        String url = fileConfig.url + fileDetails.fileName;
        try {
            ensureDirectoryExists(path.getParent());
            Files.write(path, file.getBytes());
            // Video davomiyligini faqat video fayllar uchun olish
            String videoDuration = fileConfig.attachType == Attachment.AttachType.VIDEO ? getVideoDuration(path.toString()) : "00:00";
            Attachment attachment = Attachment.builder()
                    .fileName(fileDetails.fileName)
                    .url(url)
                    .attachType(fileConfig.attachType)
                    .videoDuration(videoDuration)
                    .build();
            return attachmentRepository.save(attachment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file to disk: " + fileDetails.fileName, e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save file metadata to database", e);
        }
    }

    private String getVideoDuration(String filePath) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffprobe", "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                filePath
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new IOException("FFprobe failed with exit code: " + exitCode + " for file: " + filePath);
            }
            String line = reader.readLine();
            if (line != null && !line.trim().isEmpty()) {
                try {
                    double seconds = Double.parseDouble(line);
                    if (seconds <= 0) {
                        return "00:00";
                    }
                    int minutes = (int) (seconds / 60);
                    int remainingSeconds = (int) (seconds % 60);
                    return String.format("%02d:%02d", minutes, remainingSeconds);
                } catch (NumberFormatException e) {
                    throw new IOException("Invalid duration format from FFprobe: " + line + " for file: " + filePath, e);
                }
            }
            return "00:00";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted state
            throw new IOException("FFprobe process interrupted for file: " + filePath, e);
        } finally {
            process.destroy();
        }
    }

    @Transactional
    public Attachment saveVideoWithPoster(MultipartFile file) throws IOException {
        Attachment attachment = saveFile(file);
        if (!attachment.getAttachType().equals(Attachment.AttachType.VIDEO)) {
            return attachment;
        }
        String imageName = UUID.randomUUID() + ".png";
        Path posterPath = Paths.get(imagePath + imageName);
        String posterUrl = imageUrl + imageName;

        byte[] thumbnail = getVideoThumbnail(Paths.get(videoPath + attachment.getFileName()));
        Files.write(posterPath, thumbnail);

        attachment.setThumbnailImageUrl(posterUrl);
        return attachmentRepository.save(attachment);
    }

    public HttpEntity<?> getFileByName(String fileName, Attachment.AttachType attachType) {
        String filePath = getFilePathByType(attachType);
        Path path = Paths.get(filePath + fileName);

        if (!Files.exists(path)) {
            throw new RuntimeException("File not found: " + fileName);
        }

        try {
            // Determine Content-Type
            String contentType = MIME_TYPES.getOrDefault(attachType, MediaType.APPLICATION_OCTET_STREAM_VALUE);
            // Fallback to probeContentType for PICTURE, DOCUMENT, or ANY if needed
            if (attachType == Attachment.AttachType.PICTURE ||
                    attachType == Attachment.AttachType.DOCUMENT ||
                    attachType == Attachment.AttachType.ANY) {
                String probedContentType = Files.probeContentType(path);
                if (probedContentType != null) {
                    contentType = probedContentType;
                }
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentDisposition(ContentDisposition
                    .builder(DISPOSITION_TYPES.get(attachType))
                    .filename(fileName)
                    .build());
            headers.setCacheControl(CacheControl.noCache().mustRevalidate());
            headers.setContentLength(Files.size(path)); // Set Content-Length for better client handling

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(Files.readAllBytes(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file: " + fileName, e);
        }
    }

    public HttpEntity<?> getImageByName(String imageName) {
        return getFileByName(imageName, Attachment.AttachType.PICTURE);
    }

    public HttpEntity<?> getVideoByName(String videoName) {
        return getFileByName(videoName, Attachment.AttachType.VIDEO);
    }

    public HttpEntity<?> getAudioByName(String audioName) {
        return getFileByName(audioName, Attachment.AttachType.AUDIO);
    }

    public HttpEntity<?> getDocumentByName(String documentName) {
        return getFileByName(documentName, Attachment.AttachType.DOCUMENT);
    }

    public HttpEntity<?> getOtherFile(String fileName) {
        return getFileByName(fileName, Attachment.AttachType.ANY);
    }

    @Transactional
    public void deleteFileByAttachId(Long attachId) {
        Attachment attachment = findById(attachId);
        deleteFileByName(attachment.getFileName(), attachment.getAttachType());
        attachment.setFileName(null);
        attachment.setUrl(null);
        attachment.setAttachType(null);
        attachmentRepository.save(attachment);
    }

    public Attachment findById(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Attachment not found with id: " + id));
    }

    private FileConfig determineFileConfig(String extension) {
        if (IMAGE_EXTENSIONS.contains(extension)) {
            return new FileConfig(imagePath, imageUrl, Attachment.AttachType.PICTURE);
        } else if (VIDEO_EXTENSIONS.contains(extension)) {
            return new FileConfig(videoPath, videoUrl, Attachment.AttachType.VIDEO);
        } else if (AUDIO_EXTENSIONS.contains(extension)) {
            return new FileConfig(audioPath, audioUrl, Attachment.AttachType.AUDIO);
        } else if (DOCUMENT_EXTENSIONS.contains(extension)) {
            return new FileConfig(documentPath, documentUrl, Attachment.AttachType.DOCUMENT);
        } else {
            return new FileConfig(otherFilePath, otherUrl, Attachment.AttachType.ANY);
        }
    }

    private String getFilePathByType(Attachment.AttachType attachType) {
        return switch (attachType) {
            case PICTURE -> imagePath;
            case VIDEO -> videoPath;
            case AUDIO -> audioPath;
            case DOCUMENT -> documentPath;
            case ANY -> otherFilePath;
            default -> throw new EntityNotFoundException("Invalid attachment type");
        };
    }

    private FileNameAndExtension cleanFileName(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        String cleanedFilename = originalFilename.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
        String filenameWithoutExtension;
        String extension = "";

        int lastDotIndex = cleanedFilename.lastIndexOf('.');
        if (lastDotIndex != -1) {
            filenameWithoutExtension = cleanedFilename.substring(0, lastDotIndex);
            extension = cleanedFilename.substring(lastDotIndex).toLowerCase();
        } else {
            filenameWithoutExtension = cleanedFilename;
        }

        Integer count = attachmentRepository.countFilenamesByPrefix(filenameWithoutExtension);
        if (count > 0) {
            return new FileNameAndExtension(
                    String.format("%s(%d)%s", filenameWithoutExtension, count, extension),
                    extension
            );
        }
        return new FileNameAndExtension(cleanedFilename, extension);
    }

    private void deleteFileByName(String fileName, Attachment.AttachType attachType) {
        Path path = Paths.get(getFilePathByType(attachType) + fileName);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + fileName, e);
        }
    }

    private void ensureDirectoryExists(Path directory) throws IOException {
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
    }

    private byte[] getVideoThumbnail(Path videoPath) throws IOException {
        Path imagePath = Files.createTempFile("video_thumb_", ".jpg");
        try {
            Process process = getProcess(videoPath, imagePath);

            if (process.waitFor() != 0) {
                throw new IOException("FFmpeg failed to create thumbnail");
            }

            return Files.readAllBytes(imagePath);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while creating thumbnail", e);
        } finally {
            Files.deleteIfExists(imagePath);
        }
    }

    private static Process getProcess(Path videoPath, Path imagePath) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(
                //yuklash oldidan:
                //  "ffmpeg.exe",
                "C:\\Users\\Asilb\\ffmpeg-7.1.1-essentials_build\\bin\\ffmpeg.exe",
                "-i", videoPath.toAbsolutePath().toString(),
                "-ss", "00:00:01",
                "-vframes", "1",
                "-y",
                imagePath.toAbsolutePath().toString()
        );


        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while (reader.readLine() != null) {
                // Log output if needed
            }
        }
        return process;
    }

    private record FileConfig(String path, String url, Attachment.AttachType attachType) {
    }
}