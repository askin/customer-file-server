package ws.askin.files.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ws.askin.files.dto.UserRequest;
import ws.askin.files.dto.UserResponse;
import ws.askin.files.exception.UserIsNotAuthorizedException;
import ws.askin.files.exception.UserIsNotFoundException;
import ws.askin.files.model.User;
import ws.askin.files.service.AuthService;
import ws.askin.files.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, AuthService authService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers(@RequestHeader Long userId)
            throws UserIsNotFoundException, UserIsNotAuthorizedException {
        this.authService.checkUserIsAdmin(userId);
        List<User> allUsers = this.userService.getAllUsers();
        List<UserResponse> response = allUsers.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        // FIXME: uniq username and email
        User user = this.userService.createUser(userRequest);
        UserResponse userResponse = this.modelMapper.map(user, UserResponse.class);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
}