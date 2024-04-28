package pl.dudios.debtor.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.dudios.debtor.customer.CustomerMapper;
import pl.dudios.debtor.customer.controller.CustomerRequest;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.model.CustomerDTO;
import pl.dudios.debtor.customer.model.Role;
import pl.dudios.debtor.customer.repository.CustomerDao;
import pl.dudios.debtor.exception.DuplicateResourceException;
import pl.dudios.debtor.exception.RequestValidationException;
import pl.dudios.debtor.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;

    public Page<CustomerDTO> getCustomers(int page, int size) {
        Page<Customer> customers = customerDao.getCustomers(page, size);
        List<CustomerDTO> customerDTOs = customers.getContent()
                .stream()
                .map(CustomerMapper::mapToCustomerDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(customerDTOs, PageRequest.of(page, size), customers.getTotalElements());
    }

    public CustomerDTO getCustomerById(final Long id) {
        Customer customer = customerDao.getCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found"));

        return CustomerMapper.mapToCustomerDTO(customer);
    }

    public Customer addCustomer(CustomerRequest request) {

        if (customerDao.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already taken");
        }
        return customerDao.insertCustomer(Customer.builder()
                .name(request.name())
                .email(request.email())
                .age(request.age())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.ROLE_USER)
                .accountNonLocked(true)
                .enabled(true) //TODO dorobić
                .build());
    }

    public void deleteCustomerById(final Long id) {
        if (!customerDao.existsById(id)) {
            throw new ResourceNotFoundException("Customer with id: " + id + " not found");
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Long id, CustomerRequest request) {
        Customer oldCustomer = customerDao.getCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with id: " + id + " not found"));
        validateRequest(request, oldCustomer);

        customerDao.updateCustomer(oldCustomer);
    }

    private void validateRequest(CustomerRequest request, Customer oldCustomer) {

        boolean changed = false;

        if (request.email() != null && !request.email().isBlank() && !request.email().equals(oldCustomer.getEmail())) {
            if (customerDao.existsByEmail(request.email()) && !request.email().equals(oldCustomer.getEmail())) {
                throw new DuplicateResourceException("Email already taken");
            }
            oldCustomer.setEmail(request.email().trim());
            changed = true;
        }
        if (request.name() != null && !request.name().isBlank() && !request.name().equals(oldCustomer.getName())) {
            oldCustomer.setName(request.name().trim());
            changed = true;
        }
        if (request.age() != null && !request.age().equals(oldCustomer.getAge())) {
            oldCustomer.setAge(request.age());
            changed = true;
        }

        if (!changed)
            throw new RequestValidationException("No changes provided");
    }


}
