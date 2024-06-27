package pl.dudios.debtor.friends.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.dudios.debtor.customer.model.Customer;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CUSTOMER_FRIENDS")
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id")
    private Customer friend;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendShipStatus status;
}
