package ws.askin.files.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ws.askin.files.enums.UserRole;
import ws.askin.files.exception.UserIsNotAuthorizedException;
import ws.askin.files.exception.UserIsNotFoundException;
import ws.askin.files.model.User;
import ws.askin.files.repository.UserRepository;

import java.util.Objects;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User checkUserIsAdmin(Long userId) {

        if (Objects.isNull(userId)) {
            throw new UserIsNotAuthorizedException();
        }

        User user = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new UserIsNotFoundException(userId));

        if (!user.getRole().equals(UserRole.ADMIN)) {
            throw new UserIsNotAuthorizedException(userId);
        }

        return user;
    }
}
