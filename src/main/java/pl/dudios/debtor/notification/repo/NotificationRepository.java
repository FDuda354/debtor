package pl.dudios.debtor.notification.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.dudios.debtor.notification.model.Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT * FROM (" +
            "    SELECT * FROM NOTIFICATIONS " +
            "    WHERE (customer_id IS NULL OR customer_id = :customerId) " +
            "    AND status = 'UNREAD' " +
            "    ORDER BY date DESC" +
            ") AS unread_notifications " +
            "UNION ALL " +
            "SELECT * FROM (" +
            "    SELECT * FROM NOTIFICATIONS " +
            "    WHERE (customer_id IS NULL OR customer_id = :customerId) " +
            "    AND status = 'READ' " +
            "    ORDER BY date DESC " +
            "    LIMIT GREATEST(50 - (SELECT COUNT(*) FROM NOTIFICATIONS WHERE (customer_id IS NULL OR customer_id = :customerId) AND status = 'UNREAD'), 0)" +
            ") AS read_notifications " +
            "ORDER BY date DESC " +
            "LIMIT 100", nativeQuery = true)
    List<Notification> getLastNotificationsByCustomerId(Long customerId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE NOTIFICATIONS SET status = 'READ' WHERE customer_id = :customerId", nativeQuery = true)
    void readNotificationsByCustomerId(Long customerId);

}
