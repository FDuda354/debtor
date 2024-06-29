package pl.dudios.debtor.friends.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.dudios.debtor.customer.model.Customer;
import pl.dudios.debtor.friends.model.Friendship;

import java.util.List;
import java.util.Optional;

public interface FriendshipRepo extends JpaRepository<Friendship, Long> {

    @Query("SELECT c FROM Customer c " +
            "JOIN Friendship f ON (c.id = f.friend.id AND f.customer.id = :customerId) " +
            "OR (c.id = f.customer.id AND f.friend.id = :customerId) " +
            "WHERE f.status = 'ACCEPTED' "+
            "ORDER BY c.firstName, c.surname")
    Page<Customer> findFriendsByCustomerId(@Param("customerId") Long customerId, Pageable pageable);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END " +
            "FROM customer_friends cf " +
            "WHERE (cf.customer_id = :customerId AND cf.friend_id = :friendId) " +
            "OR (cf.customer_id = :friendId AND cf.friend_id = :customerId)",
            nativeQuery = true)
    Boolean friendshipExists(Long customerId, Long friendId);

    @Query("SELECT f FROM Friendship f " +
            "WHERE (f.customer.id = :customerId AND f.friend.id = :friendId) OR (f.customer.id = :friendId AND f.friend.id = :customerId) AND f.status = 'ACCEPTED' ")
    Optional<Friendship> findByCustomerIdAndFriendId(Long customerId, Long friendId);
}
