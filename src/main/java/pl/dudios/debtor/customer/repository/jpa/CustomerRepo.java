package pl.dudios.debtor.customer.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.dudios.debtor.customer.model.Customer;

import java.util.Optional;


public interface CustomerRepo extends JpaRepository<Customer, Long> {
    boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);
}
