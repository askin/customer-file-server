package ws.askin.files.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ws.askin.files.dto.FileRequest;
import ws.askin.files.dto.FileResponse;
import ws.askin.files.exception.FileIsNotFoundException;
import ws.askin.files.exception.UserIsNotAuthorizedException;
import ws.askin.files.exception.UserIsNotFoundException;
import ws.askin.files.model.File;
import ws.askin.files.model.User;
import ws.askin.files.service.AuthService;
import ws.askin.files.service.FileService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileController {

    private final AuthService authService;
    private final FileService fileService;
    private final ModelMapper modelMapper;

    public FileController(AuthService authService, FileService fileService, ModelMapper modelMapper) {
        this.authService = authService;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<FileResponse>> getAllFiles(@RequestHeader Long userId)
            throws UserIsNotAuthorizedException, UserIsNotFoundException {
        this.authService.checkUserIsAdmin(userId);

        List<File> allFiles = this.fileService.getAllFiles();
        List<FileResponse> filesResponse = allFiles.stream()
                .map(file -> this.modelMapper.map(file, FileResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(filesResponse, HttpStatus.OK);
    }

    @GetMapping("/fileId")
    public ResponseEntity<FileResponse> getFile(@RequestHeader Long userId, @PathVariable Long fileId)
            throws UserIsNotAuthorizedException, UserIsNotFoundException, FileIsNotFoundException {
        this.authService.checkUserIsAdmin(userId);

        File file = this.fileService.getFile(fileId);
        FileResponse fileResponse = this.modelMapper.map(file, FileResponse.class);
        return new ResponseEntity<>(fileResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<FileResponse> createFile(@RequestHeader Long userId, @RequestBody FileRequest fileRequest)
            throws UserIsNotAuthorizedException, UserIsNotFoundException {
        User user = this.authService.checkUserIsAdmin(userId);

        File file = this.fileService.createFile(fileRequest, user);
        FileResponse fileResponse = this.modelMapper.map(file, FileResponse.class);
        return new ResponseEntity<>(fileResponse, HttpStatus.CREATED);

    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity deleteFile(@RequestHeader Long userId, @PathVariable Long fileId) {
        this.authService.checkUserIsAdmin(userId);
        this.fileService.deleteFile(fileId);

        return new ResponseEntity(HttpStatus.OK);
    }
}
