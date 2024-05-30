package pl.dudios.debtor.customer.repository.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.repository.CustomerDao;
import pl.dudios.debtor.customer.repository.CustomerProjection;
import pl.dudios.debtor.exception.ResourceNotFoundException;

import java.util.Optional;


@RequiredArgsConstructor
@Repository
public class CustomerJpaDas implements CustomerDao {

    private final CustomerRepo customerRepo;

    @Override
    public Page<Customer> getCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerRepo.findAll(pageable);
    }

    @Override
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepo.findById(id);
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        CustomerProjection customerProjection = customerRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with email: " + email + " not found"));
       return Customer.builder()
               .id(customerProjection.getId())
               .surname(customerProjection.getSurname())
               .email(customerProjection.getEmail())
               .password(customerProjection.getPassword())
               .age(customerProjection.getAge())
               .role(customerProjection.getRole())
               .gender(customerProjection.getGender())
               .enabled(customerProjection.isEnabled())
               .accountNonLocked(customerProjection.isAccountNonLocked())
               .build();
    }

    @Override
    public Customer insertCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customerRepo.save(customer);
    }

    @Override
    public void deleteCustomerById(Long id) {
        customerRepo.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerRepo.existsByEmail(email);
    }

    @Override
    public boolean existsById(Long id) {
        return customerRepo.existsById(id);
    }

    @Override
    public String getProfileFileNameByCustomerId(Long id) {
        return customerRepo.findProfileImage(id);
    }
}

