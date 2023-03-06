package ws.askin.files.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ws.askin.files.dto.UserRequest;
import ws.askin.files.dto.UserUpdateRequest;
import ws.askin.files.enums.UserRole;
import ws.askin.files.exception.EmailIsAlreadyTakenException;
import ws.askin.files.exception.NullFieldException;
import ws.askin.files.exception.UserIsNotFoundException;
import ws.askin.files.exception.UserNameIsAlreadyTakenException;
import ws.askin.files.model.User;
import ws.askin.files.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ModelMapper modelMapper;

    UserService userService;


    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository, modelMapper);
        User adminUser = new User("admin", "Admin User", "admin@askin.ws", UserRole.ADMIN);
    }

    @AfterEach
    void tearDown() {
        this.userRepository.deleteAll();
    }

    @Test
    void testCreateUser_withNormalUserParameters() {
        UserRequest userRequest = new UserRequest();

        String uniqueUserName = "unique_user_name";
        String uniqueEmail = "uniqueemail@askin.ws";

        userRequest.setUserName(uniqueUserName);
        userRequest.setEmail(uniqueEmail);
        userRequest.setFullName("Normal User");

        User newUser = this.userService.createUser(userRequest);
        assertEquals(uniqueUserName, newUser.getUserName());
        assertEquals(uniqueEmail, newUser.getEmail());
        assertNotNull(newUser.getId());
        assertEquals(UserRole.USER, newUser.getRole());
    }

    @Test
    void testCreateUser_withNullUserName() {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail("uniqueemail@askin.ws");
        userRequest.setFullName("Normal User");

        assertThrows(NullFieldException.class,
                () -> this.userService.createUser(userRequest)
        );
    }

    @Test
    void testCreateUser_withNullEmail() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("unique_user_name");
        userRequest.setFullName("Normal User");

        assertThrows(NullFieldException.class,
                () -> this.userService.createUser(userRequest)
        );
    }

    @Test
    void testCreateUser_withNullFullName() {
        UserRequest userRequest = new UserRequest();
        userRequest.setUserName("unique_user_name");
        userRequest.setEmail("uniqueemail@askin.ws");

        assertThrows(NullFieldException.class,
                () -> this.userService.createUser(userRequest)
        );
    }

    @Test
    void testCreateUser_withNonUniqueUserName() {
        UserRequest userRequest1 = new UserRequest();
        userRequest1.setUserName("unique_user_name");
        userRequest1.setEmail("uniqueemail@askin.ws");
        userRequest1.setFullName("Normal User");

        UserRequest userRequest2 = new UserRequest();
        userRequest2.setUserName("unique_user_name");
        userRequest2.setEmail("uniqueemail_222@askin.ws");
        userRequest2.setFullName("Normal User");

        this.userService.createUser(userRequest1);

        assertThrows(UserNameIsAlreadyTakenException.class,
                () -> this.userService.createUser(userRequest2)
        );
    }

    @Test
    void testCreateUser_withNonUniqueEmail() {
        UserRequest userRequest1 = new UserRequest();
        userRequest1.setUserName("unique_user_name");
        userRequest1.setEmail("uniqueemail@askin.ws");
        userRequest1.setFullName("Normal User");

        UserRequest userRequest2 = new UserRequest();
        userRequest2.setUserName("unique_user_name_2");
        userRequest2.setEmail("uniqueemail@askin.ws");
        userRequest2.setFullName("Normal User");

        this.userService.createUser(userRequest1);

        assertThrows(EmailIsAlreadyTakenException.class,
                () -> this.userService.createUser(userRequest2)
        );
    }

    @Test
    void testDeleteUser_withRealUserID() {
        UserRequest userRequest = new UserRequest();

        String uniqueUserName = "unique_user_name";
        String uniqueEmail = "uniqueemail@askin.ws";

        userRequest.setUserName(uniqueUserName);
        userRequest.setEmail(uniqueEmail);
        userRequest.setFullName("Normal User");
        User newUser = this.userService.createUser(userRequest);

        this.userService.deleteUser(newUser.getId());
        User deletedUser = userService.getUser(newUser.getId());

        assertEquals(uniqueUserName, deletedUser.getUserName());
        assertEquals(uniqueEmail, deletedUser.getEmail());
        assertNotNull(deletedUser.getId());
        assertEquals(UserRole.USER, deletedUser.getRole());
        assertEquals(true, deletedUser.isDeleted());
    }

    @Test
    void testDeleteUser_withNotExistUserId() {
        assertThrows(UserIsNotFoundException.class,
                () -> this.userService.deleteUser(1000000L));
    }

    @Test
    void testUpdateUser_withCorrectValues() {
        UserRequest userRequest = new UserRequest();

        String uniqueUserName = "unique_user_name";
        String newUniqueUserName = "new_unique_user_name";
        String uniqueEmail = "uniqueemail@askin.ws";
        String newUniqueEmail = "new_uniqueemail@askin.ws";
        String newFullName = "New Normal User";

        userRequest.setUserName(uniqueUserName);
        userRequest.setEmail(uniqueEmail);
        userRequest.setFullName("Normal User");

        User newUser = this.userService.createUser(userRequest);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setRole(UserRole.ADMIN);
        userUpdateRequest.setDeleted(true);
        userUpdateRequest.setEmail(newUniqueEmail);
        userUpdateRequest.setUserName(newUniqueUserName);
        userUpdateRequest.setFullName(newFullName);

        User updatedUser = this.userService.updateUser(newUser.getId(), userUpdateRequest);
        assertEquals(newUniqueEmail, updatedUser.getEmail());
        assertEquals(newUniqueUserName, updatedUser.getUserName());
        assertEquals(newFullName, updatedUser.getFullName());
        assertEquals(true, updatedUser.isDeleted());
        assertEquals(UserRole.ADMIN, updatedUser.getRole());
    }

    @Test
    void testUpdateUser_withSameValues() {
        UserRequest userRequest = new UserRequest();

        String uniqueUserName = "unique_user_name";
        String uniqueEmail = "uniqueemail@askin.ws";
        String newFullName = "New Normal User";

        userRequest.setUserName(uniqueUserName);
        userRequest.setEmail(uniqueEmail);
        userRequest.setFullName("Normal User");

        User newUser = this.userService.createUser(userRequest);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setRole(UserRole.ADMIN);
        userUpdateRequest.setDeleted(true);
        userUpdateRequest.setEmail(uniqueEmail);
        userUpdateRequest.setUserName(uniqueUserName);
        userUpdateRequest.setFullName(newFullName);

        User updatedUser = this.userService.updateUser(newUser.getId(), userUpdateRequest);
        assertEquals(uniqueEmail, updatedUser.getEmail());
        assertEquals(uniqueUserName, updatedUser.getUserName());
        assertEquals(newFullName, updatedUser.getFullName());
        assertEquals(true, updatedUser.isDeleted());
        assertEquals(UserRole.ADMIN, updatedUser.getRole());
    }

    @Test
    void testUpdateUser_withNullRole() {
        UserRequest userRequest = new UserRequest();

        String uniqueUserName = "unique_user_name";
        String uniqueEmail = "uniqueemail@askin.ws";
        String newFullName = "New Normal User";

        userRequest.setUserName(uniqueUserName);
        userRequest.setEmail(uniqueEmail);
        userRequest.setFullName("Normal User");

        User newUser = this.userService.createUser(userRequest);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setDeleted(true);
        userUpdateRequest.setEmail(uniqueEmail);
        userUpdateRequest.setUserName(uniqueUserName);
        userUpdateRequest.setFullName(newFullName);

        assertThrows(NullFieldException.class,
                () -> this.userService.updateUser(newUser.getId(), userUpdateRequest)
        );
    }

    @Test
    void testUpdateUser_withNullEmail() {
        UserRequest userRequest = new UserRequest();

        String uniqueUserName = "unique_user_name";
        String uniqueEmail = "uniqueemail@askin.ws";
        String newFullName = "New Normal User";

        userRequest.setUserName(uniqueUserName);
        userRequest.setEmail(uniqueEmail);
        userRequest.setFullName("Normal User");

        User newUser = this.userService.createUser(userRequest);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setRole(UserRole.ADMIN);
        userUpdateRequest.setDeleted(true);
        userUpdateRequest.setUserName(uniqueUserName);
        userUpdateRequest.setFullName(newFullName);

        assertThrows(NullFieldException.class,
                () -> this.userService.updateUser(newUser.getId(), userUpdateRequest)
        );
    }

    @Test
    void testUpdateUser_withNullUserName() {
        UserRequest userRequest = new UserRequest();

        String uniqueUserName = "unique_user_name";
        String uniqueEmail = "uniqueemail@askin.ws";
        String newFullName = "New Normal User";

        userRequest.setUserName(uniqueUserName);
        userRequest.setEmail(uniqueEmail);
        userRequest.setFullName("Normal User");

        User newUser = this.userService.createUser(userRequest);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setRole(UserRole.ADMIN);
        userUpdateRequest.setDeleted(true);
        userUpdateRequest.setEmail(uniqueEmail);
        userUpdateRequest.setFullName(newFullName);

        assertThrows(NullFieldException.class,
                () -> this.userService.updateUser(newUser.getId(), userUpdateRequest)
        );
    }

    @Test
    void testUpdateUser_withNullFullName() {
        UserRequest userRequest = new UserRequest();

        String uniqueUserName = "unique_user_name";
        String uniqueEmail = "uniqueemail@askin.ws";

        userRequest.setUserName(uniqueUserName);
        userRequest.setEmail(uniqueEmail);
        userRequest.setFullName("Normal User");

        User newUser = this.userService.createUser(userRequest);

        UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
        userUpdateRequest.setRole(UserRole.ADMIN);
        userUpdateRequest.setDeleted(true);
        userUpdateRequest.setEmail(uniqueEmail);
        userUpdateRequest.setUserName(uniqueUserName);

        assertThrows(NullFieldException.class,
                () -> this.userService.updateUser(newUser.getId(), userUpdateRequest)
        );
    }
}