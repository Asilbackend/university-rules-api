package uz.tuit.unirules.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.tuit.unirules.entity.abs.roles.Role;
import uz.tuit.unirules.entity.faculty.Faculty;
import uz.tuit.unirules.entity.faculty.education_direction.EducationDirection;
import uz.tuit.unirules.entity.faculty.group.Group;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.repository.ModuleRepository;
import uz.tuit.unirules.repository.RoleRepository;
import uz.tuit.unirules.repository.UserRepository;
import uz.tuit.unirules.repository.faculty.EducationDirectionRepository;
import uz.tuit.unirules.repository.faculty.FacultyRepository;
import uz.tuit.unirules.repository.faculty.GroupRepository;

@Component
public class Runner implements CommandLineRunner {
    @Value("${server.url}")
    private String serverUrl;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;
    private static Role ADMIN = null;
    private static Role USER = null;
    private static Role SUPER = null;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final FacultyRepository facultyRepository;
    private final EducationDirectionRepository educationDirectionRepository;
    private final GroupRepository groupRepository;

    public Runner(PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                  UserRepository userRepository, ModuleRepository moduleRepository,
                  FacultyRepository facultyRepository,
                  EducationDirectionRepository educationDirectionRepository,
                  GroupRepository groupRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.moduleRepository = moduleRepository;
        this.facultyRepository = facultyRepository;
        this.educationDirectionRepository = educationDirectionRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!ddl.equals("update")) {
            saveRoles();
            saveUsers();
            saveModule();
            saveFacultyGroupEduDirection();

        }
        System.out.println(serverUrl + "swagger-ui/index.html#/");
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

    private void saveModule() {
        moduleRepository.save(Module.builder()
                .description("desc")
                .isDeleted(false)
                .moduleState(Module.ModuleState.REQUIRED)
                .name("name")
                .build());
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
    }

    private void saveRoles() {
        Role roleAdmin = new Role("ADMIN");
        Role roleStudent = new Role("STUDENT");
        Role roleSuperAdmin = new Role("SUPER_ADMIN");
        ADMIN = roleRepository.save(roleAdmin);
        USER = roleRepository.save(roleStudent);
        SUPER = roleRepository.save(roleSuperAdmin);
    }

}
