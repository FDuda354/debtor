package pl.dudios.debtor.customer.repository;

import pl.dudios.debtor.customer.model.Customer;

import java.util.Optional;


public interface CustomerDao {

    Optional<Customer> getCustomerById(Long id);

    Optional<Customer> getCustomerByEmail(String email);

    Customer insertCustomer(Customer customer);

    void updateCustomer(Customer customer);

    void deleteCustomerById(Long id);

    boolean existsByEmail(String email);

    boolean existsById(Long id);
}
