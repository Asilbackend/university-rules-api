package uz.tuit.unirules.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import uz.tuit.unirules.dto.request_dto.ContentCreateDto;
import uz.tuit.unirules.entity.abs.roles.Role;
import uz.tuit.unirules.entity.attachment.Attachment;
import uz.tuit.unirules.entity.faculty.Faculty;
import uz.tuit.unirules.entity.faculty.education_direction.EducationDirection;
import uz.tuit.unirules.entity.faculty.group.Group;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.repository.AttachmentRepository;
import uz.tuit.unirules.repository.ModuleRepository;
import uz.tuit.unirules.repository.RoleRepository;
import uz.tuit.unirules.repository.UserRepository;
import uz.tuit.unirules.repository.faculty.EducationDirectionRepository;
import uz.tuit.unirules.repository.faculty.FacultyRepository;
import uz.tuit.unirules.repository.faculty.GroupRepository;
import uz.tuit.unirules.services.attachment.AttachmentService;
import uz.tuit.unirules.services.content.ContentService;
import uz.tuit.unirules.services.module.ModuleService;


import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    @Value("${server.url}")
    private String serverUrl;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;
    @Value("${spring.profiles.active}")
    private String profileActive;
    private static Role ADMIN = null;
    private static Role STUDENT = null;
    private static Role SUPER = null;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final FacultyRepository facultyRepository;
    private final EducationDirectionRepository educationDirectionRepository;
    private final GroupRepository groupRepository;
    private final ContentService contentService;

    private final AttachmentRepository attachmentRepository;


    @Override
    public void run(String... args) {
        if (!ddl.equals("update") && !ddl.equals("none")) {
            saveRoles();
            saveUsers();
            if (profileActive.equals("dev")) {
                saveAttachmentsMockData();
                saveContentsAndModules();
            }
            saveFacultyGroupEduDirection();
        }
        System.out.println(serverUrl + "swagger-ui/index.html#/");
    }

    private void saveAttachmentsMockData() {
        try {
            Resource[] resources = new PathMatchingResourcePatternResolver()
                    .getResources("classpath:static/videos/*");

            List<Attachment> unSavedAttachments = new ArrayList<>();

            Path targetDir = Paths.get("src/main/resources/templates/videos");
            if (!Files.exists(targetDir)) {
                Files.createDirectories(targetDir);
            }

            for (Resource resource : resources) {
                String filename = resource.getFilename();
                Path targetPath = targetDir.resolve(filename);

                // Faylni input stream orqali nusxalash
                try (InputStream inputStream = resource.getInputStream()) {
                    Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (RuntimeException r) {
                    r.printStackTrace();
                }

                // Attachment entity yasash
                Attachment attachment = Attachment.builder()
                        .url(serverUrl + "api/attachment/videos/" + filename)
                        .attachType(Attachment.AttachType.VIDEO)
                        .videoDuration(null)
                        .title("title" + UUID.randomUUID())
                        .fileName(filename)
                        .isDeleted(false)
                        .build();

                unSavedAttachments.add(attachment);
            }
            attachmentRepository.saveAll(unSavedAttachments);
        } catch (IOException e) {
            throw new RuntimeException("Error copying video files to templates folder", e);
        }
    }


    private void saveContentsAndModules() {
        List<Module> unsavedModules = new ArrayList<>();

        // 1. 20 ta Module yaratish
        for (int i = 0; i < 20; i++) {
            Module module = Module.builder()
                    .name("Module-" + i)
                    .description("Mock description for module " + i)
                    .moduleState(i % 2 == 0 ? Module.ModuleState.OPTIONAL : Module.ModuleState.REQUIRED)
                    .isDeleted(false)
                    .build();
            unsavedModules.add(module);
        }

        // 2. Modulalarni saqlash
        List<Module> savedModules = moduleRepository.saveAll(unsavedModules);

        // 3. Har bir Module uchun Content yaratish
        for (int i = 0; i < savedModules.size(); i++) {
            Module module = savedModules.get(i);

            // 15 ta TextElement yaratish
            List<ContentCreateDto.TextElement> textElements = new ArrayList<>();
            for (int j = 0; j < 15; j++) {
                textElements.add(new ContentCreateDto.TextElement(
                        "TextTitle_" + UUID.randomUUID(),
                        "Body text for element " + j + " of content " + i,
                        j
                ));
            }

            // 15 ta AttachmentElement yaratish
            List<ContentCreateDto.AttachmentElement> attachmentElements = new ArrayList<>();
            for (int j = 0; j <= 6; j++) {
                attachmentElements.add(new ContentCreateDto.AttachmentElement(
                        "AttachmentTitle_" + UUID.randomUUID(),
                        (long) j + 1L, // mavjud attachmentId ni o‘rniga real mavjud ID ni berish kerak
                        j
                ));
            }

            // 4. ContentCreateDto yasash
            ContentCreateDto contentCreateDto = new ContentCreateDto(
                    module.getId(),
                    "ContentTitle_" + i + "_" + UUID.randomUUID(),
                    i % 2 == 0, // required ni false yoki true qilib berayapmiz
                    textElements,
                    attachmentElements
            );

            // 5. Content yaratish
            contentService.create(contentCreateDto);
        }
    }


    private void saveFacultyGroupEduDirection() {
        Faculty faculty = Faculty.builder()
                .description("faculty description")
                .name("Kompyuter injiniringi")
                .build();
        facultyRepository.save(faculty);
        EducationDirection multimedia = EducationDirection.builder()
                .name("Multimedia")
                .faculty(faculty)
                .build();
        educationDirectionRepository.save(multimedia);
        Group group = Group.builder()
                .educationDirection(multimedia)
                .name("219-22 MTO'")
                .build();
        groupRepository.save(group);
    }


    private void saveUsers() {
        User buildUser = User.builder()
                .firstname("user1")
                .lastname("userlas1")
                .phone("+998901234567")
                .username("1")
                .email("oktamovasilbek12@gmail.com")
                .active(true)
                .role(SUPER)
                .password(passwordEncoder.encode("1"))
                .build();
        userRepository.save(buildUser);

        User buildUser2 = User.builder()
                .firstname("Asilbek")
                .lastname("O'ktamov")
                .phone("+998919001221")
                .username("asilbek200")
                .email("oktamovasilbek270@gmail.com")
                .active(true)
                .role(STUDENT)
                .password(passwordEncoder.encode("award200"))
                .build();
        userRepository.save(buildUser2);
    }

    private void saveRoles() {
        Role roleAdmin = new Role("ADMIN");
        Role roleStudent = new Role("STUDENT");
        Role roleSuperAdmin = new Role("SUPER_ADMIN");
        ADMIN = roleRepository.save(roleAdmin);
        STUDENT = roleRepository.save(roleStudent);
        SUPER = roleRepository.save(roleSuperAdmin);
    }

}
