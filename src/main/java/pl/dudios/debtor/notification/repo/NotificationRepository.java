package pl.dudios.debtor.notification.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.dudios.debtor.notification.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
