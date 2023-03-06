package ws.askin.files.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ws.askin.files.dto.FileRequest;
import ws.askin.files.enums.UserRole;
import ws.askin.files.exception.CustomerIsNotFoundException;
import ws.askin.files.exception.FileIsNotFoundException;
import ws.askin.files.repository.CustomerRepository;
import ws.askin.files.repository.FileRepository;
import ws.askin.files.model.File;
import ws.askin.files.model.User;
import ws.askin.files.model.Customer;
import ws.askin.files.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FileServiceTest {

    @Autowired
    FileRepository fileRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CustomerRepository customerRepository;

    FileService fileService;

    @BeforeEach
    void setUp() {
        this.fileService = new FileService(this.fileRepository, userRepository, customerRepository, this.modelMapper);
    }

    @AfterEach
    void tearDown() {
        this.fileRepository.deleteAll();
        this.userRepository.deleteAll();
        this.customerRepository.deleteAll();
    }

    @Test
    void getFile_witNotExsistId() {
        assertThrows(FileIsNotFoundException.class,
                () -> this.fileService.getFile(200L)
        );
    }

    @Test
    void getFile_withId() {
        User testUser = new User("testuser", "Test User", "test@askin.ws", UserRole.ADMIN);
        this.userRepository.save(testUser);

        Customer testCustomer = new Customer("Test Customer");
        this.customerRepository.save(testCustomer);

        String fileDescription = "File Description";
        String fileName = "File Name";

        File file = new File();
        file.setUser(testUser);
        file.setCustomer(testCustomer);
        file.setDescription(fileDescription);
        file.setName(fileName);

        this.fileRepository.save(file);

        File fetchedFile = this.fileService.getFile(file.getId());

        assertEquals(file.getId(), fetchedFile.getId());
        assertEquals(file.getUser().getId(), fetchedFile.getUser().getId());
        assertEquals(file.getCustomer().getId(), fetchedFile.getCustomer().getId());
        assertEquals(fileName, fetchedFile.getName());
        assertEquals(fileDescription, fetchedFile.getDescription());
    }

    @Test
    void getAllFiles() {
        User testUser = new User("testuser", "Test User", "test@askin.ws", UserRole.ADMIN);
        this.userRepository.save(testUser);

        Customer testCustomer = new Customer("Test Customer");
        this.customerRepository.save(testCustomer);

        String fileDescription = "File Description";
        String fileName = "File Name";

        File file = new File();
        file.setUser(testUser);
        file.setCustomer(testCustomer);
        file.setDescription(fileDescription);
        file.setName(fileName);

        this.fileRepository.save(file);
        List<File> fetchedFiles = this.fileService.getAllFiles();
        assertEquals(1, fetchedFiles.size());
    }

    @Test
    void createFile_withRealCustomer() {
        User testUser = new User("testuser", "Test User", "test@askin.ws", UserRole.ADMIN);
        this.userRepository.save(testUser);

        Customer testCustomer = new Customer("Test Customer");
        this.customerRepository.save(testCustomer);

        String fileDescription = "File Description";
        String fileName = "File Name";
        String filePath = "https://askin.ws/test.file.pdf";

        FileRequest fileRequest = new FileRequest();
        fileRequest.setCustomerId(testCustomer.getId());
        fileRequest.setDescription(fileDescription);
        fileRequest.setName(fileName);
        fileRequest.setPath(filePath);

        this.fileService.createFile(fileRequest, testUser);
    }

    @Test
    void createFile_withNonExistCustomer() {
        User testUser = new User("testuser", "Test User", "test@askin.ws", UserRole.ADMIN);
        this.userRepository.save(testUser);

        String fileDescription = "File Description";
        String fileName = "File Name";
        String filePath = "https://askin.ws/test.file.pdf";

        FileRequest fileRequest = new FileRequest();
        fileRequest.setCustomerId(100000L);
        fileRequest.setDescription(fileDescription);
        fileRequest.setName(fileName);
        fileRequest.setPath(filePath);

        assertThrows(
                CustomerIsNotFoundException.class,
                () -> this.fileService.createFile(fileRequest, testUser)
        );
    }

    @Test
    void testDeleteFile_withNotExistFileId() {
        assertThrows(FileIsNotFoundException.class,
                () -> this.fileService.deleteFile(1000000L));
    }

    @Test
    void testDeleteFile_withRealData() {
        User testUser = new User("testuser", "Test User", "test@askin.ws", UserRole.ADMIN);
        this.userRepository.save(testUser);

        Customer testCustomer = new Customer("Test Customer");
        this.customerRepository.save(testCustomer);

        String fileDescription = "File Description";
        String fileName = "File Name";
        String filePath = "https://askin.ws/test.file.pdf";

        FileRequest fileRequest = new FileRequest();
        fileRequest.setCustomerId(testCustomer.getId());
        fileRequest.setDescription(fileDescription);
        fileRequest.setName(fileName);
        fileRequest.setPath(filePath);

        File fetchedFile = this.fileService.createFile(fileRequest, testUser);

        assertNotNull(fetchedFile.getId());
        assertEquals(fileName, fetchedFile.getName());
        assertEquals(fileDescription, fetchedFile.getDescription());

        this.fileService.deleteFile(fetchedFile.getId());
        File deletedFile = this.fileService.getFile(fetchedFile.getId());

        assertNotNull(deletedFile.getId());
        assertEquals(fileName, deletedFile.getName());
        assertEquals(fileDescription, deletedFile.getDescription());
        assertEquals(true, deletedFile.isDeleted());

    }
}