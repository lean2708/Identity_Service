package devteria.identity_service.configuration;



import devteria.identity_service.entity.User;
import devteria.identity_service.enums.Role;
import devteria.identity_service.repository.UserRepository;
import devteria.identity_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ApplicationInitConfig {


    private final PasswordEncoder passwordEncoder;

    // Tao user ADMIN
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if(userRepository.findByUsername("admin").isEmpty()){
                HashSet<String> roles = new HashSet<>();
                roles.add(Role.ADMIN.name());

                User user = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        // .roles(roles)
                        .build();

                userRepository.save(user);

                log.warn("admin user has been created with default password: admin");
            }
        };

    }
}
