package ws.askin.files.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ws.askin.files.dto.UserRequest;
import ws.askin.files.enums.UserRole;
import ws.askin.files.exception.UserIsNotFoundException;
import ws.askin.files.model.User;
import ws.askin.files.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public User getUser(Long userId) throws UserIsNotFoundException {
        return this.userRepository
                .findById(userId)
                .orElseThrow(() -> new UserIsNotFoundException(userId));
    }

    public List<User> getAllUsers() {
        return this.userRepository
                .findAll();
    }

    public User createUser(UserRequest userRequest) {
        User user = modelMapper.map(userRequest, User.class);
        user.setRole(UserRole.USER);
        return this.userRepository.save(user);
    }
}
