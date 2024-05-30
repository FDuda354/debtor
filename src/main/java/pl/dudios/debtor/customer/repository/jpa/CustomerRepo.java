package pl.dudios.debtor.customer.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.customer.repository.CustomerProjection;

import java.util.Optional;

public interface CustomerRepo extends JpaRepository<Customer, Long> {

    boolean existsByEmail(String email);

    @Query("SELECT c.id AS id, " +
            "c.firstName AS firstName, " +
            "c.surname AS surname, " +
            "c.email AS email, " +
            "c.password AS password, " +
            "c.age AS age, " +
            "c.role AS role, " +
            "c.gender AS gender, " +
            "c.enabled AS enabled, " +
            "c.accountNonLocked AS accountNonLocked " +
            "FROM Customer c " +
            "WHERE c.email = :email")
    Optional<CustomerProjection> findByEmail(String email);


    @Query(value = "SELECT c.profile_image" +
            " FROM customers AS c " +
            "WHERE c.id = :id",
            nativeQuery = true)
    String findProfileImage(Long id);
}
