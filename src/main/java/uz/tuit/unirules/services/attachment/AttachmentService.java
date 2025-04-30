package uz.tuit.unirules.services.attachment;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.repository.AttachmentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

@Service
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
    private final Set<String> imageExtensions = Set.of(".jpeg", ".jpg", ".png", ".gif", ".webp");
    private final Set<String> videoExtensions = Set.of(".mp4", ".webm", ".mov");
    private final Set<String> audioExtensions = Set.of(".mp3", ".wav", ".ogg");
    private final Set<String> documentExtensions = Set.of(".pdf", ".doc", ".docx", ".ppt", ".pptx", ".xls", ".xlsx", ".txt");

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Value("${image.file}")
    String imagePath;
    @Value("${video.file}")
    String videoPath;
    @Value("${audio.file}")
    String audioPath;
    @Value("${document.file}")
    String documentPath;
    @Value("${other.file}")
    String otherFilePath;


    @Value("${other.url}")
    String otherUrl;
    @Value("${image.url}")
    String imageUrl;
    @Value("${video.url}")
    String videoUrl;
    @Value("${audio.url}")
    String audioUrl;
    @Value("${document.url}")
    String documentUrl;

    @Transactional
    public Attachment saveFile(MultipartFile file) {
        // Fayl nomini tozalash
        FileNameAndExtension fileNameAndExtension = cleanFileName(file.getOriginalFilename());
        String filename = fileNameAndExtension.fileName;
        String fileExtension = fileNameAndExtension.extension;
        if (fileExtension.isBlank()) {
            throw new RuntimeException("Invalid file extension.");
        }
        // Fayl turini aniqlash
        Path path;
        String url;
        Attachment.AttachType attachType;
        if (imageExtensions.contains(fileExtension)) {
            path = Paths.get(imagePath + filename);
            url = imageUrl + filename;
            attachType = Attachment.AttachType.PICTURE;
        } else if (videoExtensions.contains(fileExtension)) {
            path = Paths.get(videoPath + filename);
            url = videoUrl + filename;
            attachType = Attachment.AttachType.VIDEO;
        } else if (audioExtensions.contains(fileExtension)) {
            path = Paths.get(audioPath + filename);
            url = audioUrl + filename;
            attachType = Attachment.AttachType.AUDIO;
        } else if (documentExtensions.contains(fileExtension)) {
            path = Paths.get(documentPath + filename);
            url = documentUrl + filename;
            attachType = Attachment.AttachType.DOCUMENT;
        } else {
            path = Paths.get(otherFilePath + filename);
            url = otherUrl + filename;
            attachType = Attachment.AttachType.ANY;
        }
        // Fayl ma'lumotini saqlash uchun Attachment yaratish
        Attachment attachment = Attachment.builder()
                .fileName(filename)
                .url(url)
                .attachType(attachType)
                .build();

        try {
            // Direktoriya mavjudligini tekshirish va kerak bo'lsa yaratish
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            // Faylni diskka yozish
            Files.write(path, file.getBytes());

            // Fayl ma'lumotlarini ma'lumotlar bazasiga saqlash
            attachment = attachmentRepository.save(attachment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file to disk", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save file metadata to the database", e);
        }
        return attachment;
    }

    private FileNameAndExtension cleanFileName(String originalFilename) {
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("Filename cannot be null or empty");
        }

        //Fayl nomini tozalash
        String cleanedFilename = originalFilename
                .trim()
                .replaceAll("[^a-zA-Z0-9._-]", "_");

        String filenameWithoutExtension;
        String extension = "";

        int lastDotIndex = cleanedFilename.lastIndexOf('.');
        if (lastDotIndex != -1) {
            filenameWithoutExtension = cleanedFilename.substring(0, lastDotIndex);
            extension = getFileExtension(cleanedFilename, lastDotIndex);
        } else {
            filenameWithoutExtension = cleanedFilename;
        }

        // DB dan barcha mos fayl nomlarini olish
        System.out.println("filenameWithoutExtension = " + filenameWithoutExtension);
        //List<String> similarFilenames = attachmentRepository.findFilenamesByPrefix(filenameWithoutExtension);
        Integer countFilenamesByPrefix = attachmentRepository.countFilenamesByPrefix(filenameWithoutExtension);
        // Nomni takrorlanmas qilish
        if (countFilenamesByPrefix != 0) {
            return new FileNameAndExtension(String.format("%s(%d)%s", filenameWithoutExtension, countFilenamesByPrefix, extension), extension);
        }
        return new FileNameAndExtension(cleanedFilename, extension);
    }

    private String getFileExtension(String fileName, int lastDotIndex) {
        //Fayl kengaytmasini olish
        return fileName.substring(lastDotIndex).toLowerCase();
    }

    public Attachment findById(Long id) {
        return attachmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("attachment not found by id = %s".formatted(id)));
    }

    public HttpEntity<?> getImageByName(String imageName) {
        Path path = Paths.get(imagePath + imageName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/jpeg"); //Rasm turi, masalan JPEG uchun
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"image.jpg\""); //Faylni yuklab olishda taklif qilinadigan nom
        try {
            byte[] bytes = Files.readAllBytes(path);
            if (!Files.exists(path))
                throw new RuntimeException("ushbu nom boyicha fayl topilmadi : " + imageName);
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpEntity<?> getVideoByName(String videoName) {
        return getFile(videoName, videoPath);
    }

    private ResponseEntity<byte[]> getFile(String fileName, String filePath) {
        Path path = Paths.get(filePath + fileName);
        if (!Files.exists(path)) {
            throw new RuntimeException("Ushbu nom bo'yicha fayl topilmadi: " + fileName);
        }
        try {
            // MIME turini avtomatik aniqlash
            String contentType = Files.probeContentType(path);
            if (contentType == null) {
                contentType = "application/octet-stream"; // Noma'lum tur uchun default
            }
            // HttpHeaders qo'shish
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            // Faylni o'qish va javob qaytarish
            byte[] bytes = Files.readAllBytes(path);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(bytes);

        } catch (IOException e) {
            throw new RuntimeException("Faylni o'qishda xatolik yuz berdi: " + e.getMessage(), e);
        }
    }

    public HttpEntity<?> getAudioByName(String audioName) {
        return getFile(audioName, audioPath);
    }

    public HttpEntity<?> getDocumentByName(String documentName) {
        return getFile(documentName, documentPath);
    }

    @Transactional
    public void deleteFileByAttachId(Long attachId) {
        Attachment attachment = attachmentRepository.findById(attachId)
                .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + attachId));
        deleteByName(attachment.getFileName(), attachment.getAttachType());
        attachment.setAttachType(null);
        attachment.setUrl(null);
        attachment.setFileName(null);
        attachmentRepository.save(attachment);
    }

    private void deleteByName(String name, Attachment.AttachType attachType) {
        Path path;
        if (attachType.equals(Attachment.AttachType.ANY)) {
            path = Paths.get(otherFilePath + name);
        } else if (attachType.equals(Attachment.AttachType.VIDEO)) {
            path = Paths.get(videoPath + name);
        } else if (attachType.equals(Attachment.AttachType.PICTURE)) {
            path = Paths.get(imagePath + name);
        } else if (attachType.equals(Attachment.AttachType.AUDIO)) {
            path = Paths.get(audioPath + name);
        } else if (attachType.equals(Attachment.AttachType.DOCUMENT)) {
            path = Paths.get(documentPath + name);
        } else {
            throw new EntityNotFoundException("fayl mavjud emas");
        }
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not delete file by this name: " + name, e);
        }
    }

    public HttpEntity<?> getOtherFile(String fileName) {
        return getFile(fileName, otherFilePath);
    }

    static class FileNameAndExtension {
        String fileName;
        String extension;

        public FileNameAndExtension(String fileName, String extension) {
            this.fileName = fileName;
            this.extension = extension;
        }
    }
}
