package ws.askin.files.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ws.askin.files.dto.CustomerRequest;
import ws.askin.files.model.Customer;
import ws.askin.files.repository.CustomerRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ModelMapper modelMapper;

    CustomerService customerService;

    @BeforeEach
    void setUp() {
        this.customerService = new CustomerService(customerRepository, modelMapper);
    }

    @AfterEach
    void tearDown() {
        this.customerRepository.deleteAll();
    }

    @Test
    void testCreateCustomer() {
        CustomerRequest customerRequest = new CustomerRequest();
        String customerFullName = "CustomerName CustomerSurname";
        customerRequest.setFullName(customerFullName);

        Customer customer = this.customerService.createCustomer(customerRequest);

        assertEquals(customerFullName, customer.getFullName());
        assertNotNull(customer.getId());
    }

    @Test
    void testGetCustomer_WithId() {
        CustomerRequest customerRequest = new CustomerRequest();
        String customerFullName = "CustomerName CustomerSurname";
        customerRequest.setFullName(customerFullName);
        Customer savedCustomer = this.customerService.createCustomer(customerRequest);

        Customer fetchedCustomer = this.customerService.getCustomer(savedCustomer.getId());

        assertEquals(savedCustomer.getId(), fetchedCustomer.getId());
        assertEquals(savedCustomer.getFullName(), fetchedCustomer.getFullName());
    }

    @Test
    void testGetCustomer_allCustomer() {
        CustomerRequest customerRequest = new CustomerRequest();
        String customerFullName = "CustomerName CustomerSurname";
        customerRequest.setFullName(customerFullName);
        Customer savedCustomer = this.customerService.createCustomer(customerRequest);

        List<Customer> allCustomers = this.customerService.getAllCustomers();
        assertEquals(1, allCustomers.size());
    }
}