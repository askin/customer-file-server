package ws.askin.files.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ws.askin.files.dto.UserRequest;
import ws.askin.files.dto.UserUpdateRequest;
import ws.askin.files.enums.UserRole;
import ws.askin.files.exception.EmailIsAlreadyTakenException;
import ws.askin.files.exception.NullFieldException;
import ws.askin.files.exception.UserIsNotFoundException;
import ws.askin.files.exception.UserNameIsAlreadyTakenException;
import ws.askin.files.model.User;
import ws.askin.files.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    public User updateUser(Long targetUserId, UserUpdateRequest userUpdateRequest) {
        this.validateUserUpdateRequest(userUpdateRequest);

        this.checkEmailAddressIsUnique(userUpdateRequest.getEmail(), targetUserId);
        this.checkUserNameIsUnique(userUpdateRequest.getUserName(), targetUserId);

        User user = this.userRepository
                .findById(targetUserId)
                .orElseThrow(() -> new UserIsNotFoundException(targetUserId));

        user.setUserName(userUpdateRequest.getUserName());
        user.setEmail(userUpdateRequest.getEmail());
        user.setRole(userUpdateRequest.getRole());
        user.setDeleted(userUpdateRequest.isDeleted());
        user.setFullName(userUpdateRequest.getFullName());
        return this.userRepository.save(user);
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

    private boolean validateUserUpdateRequest(UserUpdateRequest userUpdateRequest) {
        if (Objects.isNull(userUpdateRequest.getUserName())) {
            throw new NullFieldException("userName");
        } else if (Objects.isNull(userUpdateRequest.getEmail())) {
            throw new NullFieldException("email");
        } else if (Objects.isNull((userUpdateRequest.getFullName()))) {
            throw new NullFieldException("fullName");
        } else if (Objects.isNull((userUpdateRequest.getRole()))) {
            throw new NullFieldException("role");
        }

        return true;
    }

    private boolean checkEmailAddressIsUnique(String email) {
        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new EmailIsAlreadyTakenException(email);
        }

        return true;
    }

    private boolean checkEmailAddressIsUnique(String email, Long userId) {
        Optional<User> userOptional = this.userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getId().equals(userId)) {
                throw new EmailIsAlreadyTakenException(email);
            }
        }

        return true;
    }

    private boolean checkUserNameIsUnique(String userName) {
        if (this.userRepository.findByUserName(userName).isPresent()) {
            throw new UserNameIsAlreadyTakenException(userName);
        }

        return true;
    }

    private boolean checkUserNameIsUnique(String userName, Long userId) {
        Optional<User> optionalUser = this.userRepository.findByUserName(userName);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getId().equals(userId)) {
                throw new UserNameIsAlreadyTakenException(userName);
            }
        }

        return true;
    }
}
