package pl.dudios.debtor.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.dudios.debtor.customer.repository.CustomerRepo;
import pl.dudios.debtor.notification.model.Notification;
import pl.dudios.debtor.notification.model.Status;
import pl.dudios.debtor.notification.repo.NotificationRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final CustomerRepo customerRepo;

    public void notify(Notification notification) {
        prepareNotification(notification);
        messagingTemplate.convertAndSend("/all/messages", notification);
        notificationRepository.save(notification);
    }

    public void notifyUser(String email, Notification notification) {
        prepareNotification(notification);
        notification.setCustomer(customerRepo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found")));
        messagingTemplate.convertAndSendToUser(notification.getCustomer().getEmail(), "/one/messages", notification);
        notificationRepository.save(notification);

    }

    private void prepareNotification(Notification notification) {
        notification.setStatus(Status.UNREAD);
        notification.setDate(LocalDateTime.now());
    }

    public List<Notification> getLastNotificationsByCustomerId(Long customer_id) {
        return notificationRepository.getLastNotificationsByCustomerId(customer_id);
    }

    public void readNotificationsByCustomerId(Long customer_id) {
        notificationRepository.readNotificationsByCustomerId(customer_id);

    }

    public void deleteNotificationById(Long id) {
        notificationRepository.deleteById(id);
    }
}
