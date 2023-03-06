package ws.askin.files.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ws.askin.files.dto.CustomerRequest;
import ws.askin.files.dto.CustomerResponse;
import ws.askin.files.dto.CustomerUpdateRequest;
import ws.askin.files.exception.CustomerIsNotFoundException;
import ws.askin.files.exception.UserIsNotAuthorizedException;
import ws.askin.files.exception.UserIsNotFoundException;
import ws.askin.files.model.Customer;
import ws.askin.files.service.AuthService;
import ws.askin.files.service.CustomerService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final AuthService authService;
    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public CustomerController(CustomerService customerService, AuthService authService, ModelMapper modelMapper) {
        this.authService = authService;
        this.modelMapper = modelMapper;
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers(@RequestHeader Long userId)
            throws UserIsNotFoundException, UserIsNotAuthorizedException {
        this.authService.checkUserIsAdmin(userId);

        List<Customer> customers = this.customerService.getAllCustomers();
        List<CustomerResponse> customersResponse = customers.stream()
                .map(customer -> modelMapper.map(customer, CustomerResponse.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(customersResponse, HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> getCustomer(@RequestHeader Long userId, @PathVariable Long customerId)
            throws UserIsNotFoundException, UserIsNotAuthorizedException, CustomerIsNotFoundException {
        this.authService.checkUserIsAdmin(userId);

        Customer customer = this.customerService.getCustomer(customerId);
        CustomerResponse customerResponse = this.modelMapper.map(customer, CustomerResponse.class);

        return new ResponseEntity<>(customerResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestHeader Long userId, @RequestBody CustomerRequest customerRequest)
            throws UserIsNotFoundException, UserIsNotAuthorizedException {

        this.authService.checkUserIsAdmin(userId);

        Customer customer = this.customerService.createCustomer(customerRequest);
        CustomerResponse customerResponse = this.modelMapper.map(customer, CustomerResponse.class);
        return new ResponseEntity<>(customerResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity deleteCustomer(@RequestHeader Long userId, @PathVariable Long customerId) {
        this.authService.checkUserIsAdmin(userId);
        this.customerService.deleteCustomer(customerId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(@RequestHeader Long userId, @PathVariable Long customerId, @RequestBody CustomerUpdateRequest customerUpdateRequest) {
        this.authService.checkUserIsAdmin(userId);
        Customer customer = this.customerService.updateCustomer(customerId, customerUpdateRequest);
        CustomerResponse customerResponse = this.modelMapper.map(customer, CustomerResponse.class);

        return new ResponseEntity(customerResponse, HttpStatus.OK);
    }

}
