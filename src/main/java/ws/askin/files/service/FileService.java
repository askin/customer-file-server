package ws.askin.files.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ws.askin.files.dto.FileRequest;
import ws.askin.files.dto.FileUpdateRequest;
import ws.askin.files.exception.CustomerIsNotFoundException;
import ws.askin.files.exception.FileIsNotFoundException;
import ws.askin.files.exception.NullFieldException;
import ws.askin.files.exception.UserIsNotFoundException;
import ws.askin.files.model.Customer;
import ws.askin.files.model.File;
import ws.askin.files.model.User;
import ws.askin.files.repository.CustomerRepository;
import ws.askin.files.repository.FileRepository;
import ws.askin.files.repository.UserRepository;

import java.util.List;
import java.util.Objects;

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

    public void deleteFile(Long fileId) {
        File file = this.fileRepository
                .findById(fileId)
                .orElseThrow(() -> new FileIsNotFoundException(fileId));

        file.setDeleted(true);
        this.fileRepository.save(file);
    }

    public File updateFile(Long fileId, FileUpdateRequest fileUpdateRequest) {
        if (Objects.isNull(fileUpdateRequest)) {
            throw new NullFieldException("fileUpdateRequest");
        } else if (Objects.isNull(fileId)) {
            throw new NullFieldException("fileId");
        } else if (Objects.isNull(fileUpdateRequest.getDescription())) {
            throw new NullFieldException("description");
        } else if (Objects.isNull(fileUpdateRequest.getName())) {
            throw new NullFieldException("name");
        } else if (Objects.isNull(fileUpdateRequest.getCustomerId())) {
            throw new NullFieldException("customerId");
        }

        File file = this.fileRepository
                .findById(fileId)
                .orElseThrow(() -> new FileIsNotFoundException(fileId));

        if (file.getCustomer().getId() != fileUpdateRequest.getCustomerId()) {
            Customer customer = this.customerRepository.
                    findById(fileUpdateRequest.getCustomerId())
                    .orElseThrow(() -> new CustomerIsNotFoundException(fileUpdateRequest.getCustomerId()));

            file.setCustomer(customer);
        }

        file.setName(fileUpdateRequest.getName());
        file.setDescription(fileUpdateRequest.getDescription());
        file.setDeleted(fileUpdateRequest.isDeleted());

        return this.fileRepository.save(file);
    }
}
