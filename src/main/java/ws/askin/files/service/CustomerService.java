package ws.askin.files.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ws.askin.files.dto.CustomerRequest;
import ws.askin.files.dto.CustomerUpdateRequest;
import ws.askin.files.exception.CustomerIsNotFoundException;
import ws.askin.files.exception.NullFieldException;
import ws.askin.files.model.Customer;
import ws.askin.files.repository.CustomerRepository;

import java.util.List;
import java.util.Objects;

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

    public Customer getCustomer(Long customerId) {
        return this.customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerIsNotFoundException(customerId));
    }

    public void deleteCustomer(Long customerId) {
        Customer customer = this.customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerIsNotFoundException(customerId));

        customer.setDeleted(true);
        this.customerRepository.save(customer);
    }

    public Customer updateCustomer(Long customerId, CustomerUpdateRequest customerUpdateRequest) {
        if (Objects.isNull(customerUpdateRequest)) {
            throw new NullFieldException("CustomerUpdateRequest");
        } else if (Objects.isNull(customerUpdateRequest.getFullName())) {
            throw new NullFieldException("fullName");
        } else if (Objects.isNull(customerId)) {
            throw new NullFieldException("customerId");
        }

        Customer customer = this.customerRepository
                .findById(customerId)
                .orElseThrow(() -> new CustomerIsNotFoundException(customerId));

        customer.setDeleted(customerUpdateRequest.isDeleted());
        customer.setFullName(customerUpdateRequest.getFullName());

        return this.customerRepository.save(customer);
     }
}
