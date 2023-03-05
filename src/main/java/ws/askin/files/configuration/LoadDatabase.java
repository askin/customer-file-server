package ws.askin.files.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ws.askin.files.enums.UserRole;
import ws.askin.files.model.User;
import ws.askin.files.repository.UserRepository;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository repository) {

        User adminUser = new User("admin", "Admin User", "admin@askin.ws", UserRole.ADMIN);
        User normalUser = new User("normaluser", "Normal User", "normal@askin.ws", UserRole.USER);

        return args -> {
            log.info("Preloading " + repository.save(adminUser));
            log.info("Preloading " + repository.save(normalUser));
        };
    }
}