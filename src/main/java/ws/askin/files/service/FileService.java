package ws.askin.files.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ws.askin.files.dto.FileRequest;
import ws.askin.files.exception.CustomerIsNotFoundException;
import ws.askin.files.exception.FileIsNotFoundException;
import ws.askin.files.exception.UserIsNotFoundException;
import ws.askin.files.model.File;
import ws.askin.files.model.User;
import ws.askin.files.repository.CustomerRepository;
import ws.askin.files.repository.FileRepository;
import ws.askin.files.repository.UserRepository;

import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public FileService(FileRepository fileRepository, UserRepository userRepository, CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.fileRepository = fileRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public File getFile(Long fileId) {
        return this.fileRepository
                .findById(fileId)
                .orElseThrow(() -> new FileIsNotFoundException(fileId));
    }

    public List<File> getAllFiles() {
        return this.fileRepository
                .findAll();
    }

    public File createFile(FileRequest fileRequest, User user) {

        if (!this.userRepository.findById(user.getId()).isPresent()) {
            throw new UserIsNotFoundException(user.getId());
        } else if (!this.customerRepository.findById(fileRequest.getCustomerId()).isPresent()) {
            throw new CustomerIsNotFoundException(fileRequest.getCustomerId());
        }

        File file = modelMapper.map(fileRequest, File.class);
        file.setUser(user);
        return this.fileRepository.save(file);
    }
}
