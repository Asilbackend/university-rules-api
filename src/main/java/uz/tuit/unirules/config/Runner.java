package uz.tuit.unirules.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.tuit.unirules.entity.abs.roles.Role;
import uz.tuit.unirules.entity.modul.Module;
import uz.tuit.unirules.entity.user.User;
import uz.tuit.unirules.repository.ModuleRepository;
import uz.tuit.unirules.repository.RoleRepository;
import uz.tuit.unirules.repository.UserRepository;

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

    public Runner(PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                  UserRepository userRepository, ModuleRepository moduleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!ddl.equals("update")) {
            saveRoles();
            saveUsers();
            saveModule();
        }
        System.out.println(serverUrl + "swagger-ui/index.html#/");
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
        Role roleUser = new Role("USER");
        Role roleSuperAdmin = new Role("SUPER_ADMIN");
        ADMIN = roleRepository.save(roleAdmin);
        USER = roleRepository.save(roleUser);
        SUPER = roleRepository.save(roleSuperAdmin);
    }

}
