package ws.askin.files.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ws.askin.files.enums.UserRole;
import ws.askin.files.exception.UserIsNotAuthorizedException;
import ws.askin.files.exception.UserIsNotFoundException;
import ws.askin.files.model.User;
import ws.askin.files.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    UserRepository userRepository;

    AuthService authService;

    @BeforeEach
    void setUp() {
        this.authService = new AuthService(this.userRepository);
    }

    @AfterEach
    void tearDown() {
        this.userRepository.deleteAll();
    }

    @Test
    void checkUserIsAdmin_withNullUserId() {
        assertThrows(UserIsNotAuthorizedException.class,
                () -> this.authService.checkUserIsAdmin(null));
    }

    @Test
    void checkUserIsAdmin_withNotExistUserId() {
        assertThrows(UserIsNotFoundException.class,
                () -> this.authService.checkUserIsAdmin(1000000L));
    }

    @Test
    void checkUserIsAdmin_withUserId() {
        User adminUser = new User("testadmin", "Admin User", "testadmin@askin.ws", UserRole.ADMIN);
        this.userRepository.save(adminUser);
        assertNotNull(this.authService.checkUserIsAdmin(adminUser.getId()));
    }

    @Test
    void checkUserIsAdmin_notAdminUserId() {
        User notAdminUser = new User("notadmin", "Not Admin User", "notadmin@askin.ws", UserRole.USER);
        this.userRepository.save(notAdminUser);
        assertThrows(UserIsNotAuthorizedException.class,
                () -> this.authService.checkUserIsAdmin(notAdminUser.getId()));
    }
}