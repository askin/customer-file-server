package ws.askin.files.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ws.askin.files.dto.UserRequest;
import ws.askin.files.enums.UserRole;
import ws.askin.files.exception.EmailIsAlreadyTakenException;
import ws.askin.files.exception.NullFieldException;
import ws.askin.files.exception.UserIsNotFoundException;
import ws.askin.files.exception.UserNameIsAlreadyTakenException;
import ws.askin.files.model.User;
import ws.askin.files.repository.UserRepository;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public User getUser(Long userId) {
        return this.userRepository
                .findById(userId)
                .orElseThrow(() -> new UserIsNotFoundException(userId));
    }

    public List<User> getAllUsers() {
        return this.userRepository
                .findAll();
    }

    public User createUser(UserRequest userRequest) {
        this.validateUserRequest(userRequest);

        User user = modelMapper.map(userRequest, User.class);
        user.setRole(UserRole.USER);
        return this.userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = this.userRepository
                .findById(userId)
                .orElseThrow(() -> new UserIsNotFoundException(userId));

        user.setDeleted(true);
        this.userRepository.save(user);
    }

    private boolean validateUserRequest(UserRequest userRequest) {
        if (Objects.isNull(userRequest.getUserName())) {
            throw new NullFieldException("userName");
        } else if (Objects.isNull(userRequest.getEmail())) {
            throw new NullFieldException("email");
        } else if (Objects.isNull((userRequest.getFullName()))) {
            throw new NullFieldException("fullName");
        }

        this.checkEmailAddressIsUnique(userRequest.getEmail());
        this.checkUserNameIsUnique(userRequest.getUserName());

        return true;
    }

    private boolean checkEmailAddressIsUnique(String email) {
        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new EmailIsAlreadyTakenException(email);
        }

        return true;
    }

    private boolean checkUserNameIsUnique(String userName) {
        if (this.userRepository.findByUserName(userName).isPresent()) {
            throw new UserNameIsAlreadyTakenException(userName);
        }

        return true;
    }
}
