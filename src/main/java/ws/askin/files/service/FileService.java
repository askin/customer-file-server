package ws.askin.files.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ws.askin.files.dto.FileRequest;
import ws.askin.files.exception.FileIsNotFoundException;
import ws.askin.files.model.File;
import ws.askin.files.model.User;
import ws.askin.files.repository.FileRepository;

import java.util.List;

@Service
public class FileService {
    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    public FileService(FileRepository fileRepository, ModelMapper modelMapper) {
        this.fileRepository = fileRepository;
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

    public File createFile(FileRequest FileRequest, User user) {
        File file = modelMapper.map(FileRequest, File.class);
        file.setUser(user);
        return this.fileRepository.save(file);
    }
}
