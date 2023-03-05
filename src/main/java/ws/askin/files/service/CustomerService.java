package ws.askin.files.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import ws.askin.files.dto.CustomerRequest;
import ws.askin.files.exception.CustomerIsNotFoundException;
import ws.askin.files.model.Customer;
import ws.askin.files.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public CustomerService(CustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    public List<Customer> getAllCustomers() {
        return this.customerRepository.findAll();
    }

    public Customer createCustomer(CustomerRequest customerRequest) {
        Customer customer = this.modelMapper.map(customerRequest, Customer.class);
        return this.customerRepository.save(customer);
    }

    public Customer getCustomer(Long customerId) throws CustomerIsNotFoundException {
        return this.customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerIsNotFoundException(customerId));
    }
}
